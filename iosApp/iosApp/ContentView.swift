import SwiftUI
import Shared

struct ContentView: View {
    @State private var showContent = false

    @StateViewModel var viewModel : SharedTestViewModel = ViewModelProvider().provideSharedTestViewModel()

    var body: some View {
        VStack {



                VStack(spacing: 16) {
                    Image(systemName: "swift")
                        .font(.system(size: 80)) // Reduced size
                        .foregroundColor(.accentColor)

                    Text("SwiftUI iOS App")
                        .font(.largeTitle)

                    // Use the TimerView which consumes the SharedTestViewModel
                    TimerView(viewModel: viewModel)
                }
                .transition(.move(edge: .top).combined(with: .opacity))

        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}

