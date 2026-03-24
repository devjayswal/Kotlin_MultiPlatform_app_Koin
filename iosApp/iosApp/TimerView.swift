import SwiftUI
import Shared

struct TimerView: View {
    let viewModel: SharedTestViewModel

    @State private var uiState: MainUiState = MainUiState(
        news: [],
        users: [],
        isLoading: false,
        error: nil,
        days: 0,
        hours: 0,
        minutes: 0,
        seconds: 0,
        isTimerRunning: false
    )

    var body: some View {
        VStack(spacing: 20) {

            // Timer Display
            HStack(spacing: 12) {
                TimeUnitView(value: uiState.days, label: "Days")
                TimeUnitView(value: uiState.hours, label: "Hours")
                TimeUnitView(value: uiState.minutes, label: "Mins")
                TimeUnitView(value: uiState.seconds, label: "Secs")
            }
            .padding()

            // Controls
            HStack(spacing: 20) {
                Button(action: {
                    viewModel.startTimer()
                }) {
                    Text("Start")
                        .font(.headline)
                        .padding()
                        .frame(minWidth: 100)
                        .background(uiState.isTimerRunning ? Color.gray : Color.blue)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                }
                .disabled(uiState.isTimerRunning)

                Button(action: {
                    viewModel.stopTimer()
                }) {
                    Text("Stop")
                        .font(.headline)
                        .padding()
                        .frame(minWidth: 100)
                        .background(Color.red)
                        .foregroundColor(.white)
                        .cornerRadius(10)
                }
            }

            Divider()

            Text("News Shared List")
                .font(.title2)
                .padding(.top)

            if uiState.isLoading {
                ProgressView()
                    .padding()
            } else if let error = uiState.error {
                Text(error)
                    .foregroundColor(.red)
                    .padding()
            } else {
                List(uiState.news, id: \.self) { item in
                    VStack(alignment: .leading) {
                        Text(item.title)
                            .font(.headline)
                        if let summary = item.summary {
                            Text(summary)
                                .font(.subheadline)
                                .foregroundColor(.secondary)
                        }
                    }
                }
                .listStyle(.plain)
            }
        }
        .task {
            // Collect the StateFlow from Kotlin
            let collector = Collector<MainUiState> { state in
                self.uiState = state
            }

            // Start collection
            // Note: collect is a suspend function, in Swift it takes a completion handler
            // or we use async/await if supported by the bridging.
            // Standard KMP-native-coroutines might make this easier, but here is the raw way:
            do {
                try await viewModel.uiState.collect(collector: collector)
            } catch {
                print("Error collecting state: \(error)")
            }
        }
    }
}

struct TimeUnitView: View {
    let value: Int32
    let label: String

    var body: some View {
        VStack {
            Text("\(value)")
                .font(.system(size: 24, weight: .bold, design: .monospaced))
            Text(label)
                .font(.caption)
                .foregroundColor(.secondary)
        }
        .frame(width: 60, height: 60)
        .background(Color(.systemGray6))
        .cornerRadius(12)
    }
}

// Simple generic collector for Kotlin Flows
class Collector<T>: Kotlinx_coroutines_coreFlowCollector {
    let callback: (T) -> Void

    init(callback: @escaping (T) -> Void) {
        self.callback = callback
    }

    func emit(value: Any?, completionHandler: @escaping (Error?) -> Void) {
        if let value = value as? T {
            callback(value)
        }
        completionHandler(nil)
    }
}

