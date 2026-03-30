const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
const port = 3000;

app.use(cors());
app.use(bodyParser.json());

// --- Simple Request Logger Middleware ---
app.use((req, res, next) => {
    const timestamp = new Date().toISOString();
    console.log(`[${timestamp}] ${req.method} ${req.url}`);
    if (req.body && Object.keys(req.body).length > 0) {
        // Mask password in logs for security
        const logBody = { ...req.body };
        if (logBody.password) logBody.password = '********';
        console.log(`  Body: ${JSON.stringify(logBody)}`);
    }
    next();
});

// Realistic Mock Data
const newsItems = [
    {
        id: 1,
        title: "SpaceX Starship Prepares for Next Flight",
        summary: "Engineers at Starbase are working around the clock to prepare the world's most powerful rocket for its upcoming test flight, focusing on heat shield improvements.",
        image_url: "https://picsum.photos/800/400?random=11",
        published_at: "2024-03-28T10:00:00Z",
        news_site: "SpaceX Insider",
        url: "https://example.com/news/1",
        authors: [{ name: "Elon T. Musk" }],
        updated_at: "2024-03-28T12:00:00Z"
    },
    {
        id: 2,
        title: "James Webb Telescope Discovers Ancient Galaxy",
        summary: "The JWST has captured light from a galaxy formed just 300 million years after the Big Bang, challenging current models of galaxy formation.",
        image_url: "https://picsum.photos/800/400?random=22",
        published_at: "2024-03-29T14:30:00Z",
        news_site: "Cosmos Today",
        url: "https://example.com/news/2",
        authors: [{ name: "Dr. Sarah Vance" }],
        updated_at: "2024-03-29T14:30:00Z"
    },
    {
        id: 3,
        title: "Quantum Computing Breakthrough in Silicon",
        summary: "Researchers have achieved 99.9% fidelity in two-qubit gates using standard silicon manufacturing processes, a major step toward scaling.",
        image_url: "https://picsum.photos/800/400?random=33",
        published_at: "2024-03-30T09:15:00Z",
        news_site: "Tech Frontiers",
        url: "https://example.com/news/3",
        authors: [{ name: "Marcus Chen" }],
        updated_at: "2024-03-30T09:15:00Z"
    },
    {
        id: 4,
        title: "AI Regulation: New Global Standards Proposed",
        summary: "A coalition of 20 countries has proposed a framework for AI safety and ethics to prevent misuse while fostering innovation.",
        image_url: "https://picsum.photos/800/400?random=44",
        published_at: "2024-03-30T11:00:00Z",
        news_site: "Global Policy Watch",
        url: "https://example.com/news/4",
        authors: [{ name: "Elena Rossi" }],
        updated_at: "2024-03-30T11:00:00Z"
    },
    {
        id: 5,
        title: "Electric Aviation: First Regional Flight Successful",
        summary: "A 19-seater electric aircraft completed a 200-mile journey today, marking a milestone for sustainable short-haul travel.",
        image_url: "https://picsum.photos/800/400?random=55",
        published_at: "2024-03-30T15:45:00Z",
        news_site: "Eco Transit",
        url: "https://example.com/news/5",
        authors: [{ name: "James Holden" }],
        updated_at: "2024-03-30T15:45:00Z"
    }
];

const users = [
    {
        id: 1,
        firstName: "Jane",
        lastName: "Doe",
        email: "jane.doe@example.com",
        city: "San Francisco",
        gender: "Female",
        language: "English",
        address: "456 Market St, CA 94103"
    },
    {
        id: 2,
        firstName: "John",
        lastName: "Smith",
        email: "john.smith@techcorp.com",
        city: "London",
        gender: "Male",
        language: "English",
        address: "10 Downing Street"
    },
    {
        id: 3,
        firstName: "Yuki",
        lastName: "Tanaka",
        email: "y.tanaka@tokyo-net.jp",
        city: "Tokyo",
        gender: "Non-binary",
        language: "Japanese",
        address: "Shibuya-ku, 1-2-3"
    }
];

// Endpoints

// News List with Pagination support
app.get('/articles/', (req, res) => {
    const limit = parseInt(req.query.limit) || 10;
    const offset = parseInt(req.query.offset) || 0;

    const results = newsItems.slice(offset, offset + limit);

    res.json({
        count: newsItems.length,
        next: (offset + limit < newsItems.length) ? `http://${req.headers.host}/articles/?limit=${limit}&offset=${offset + limit}` : null,
        previous: (offset > 0) ? `http://${req.headers.host}/articles/?limit=${limit}&offset=${Math.max(0, offset - limit)}` : null,
        results: results
    });
});

// News Detail
app.get('/articles/:id/', (req, res) => {
    const id = parseInt(req.params.id);
    const item = newsItems.find(i => i.id === id);
    if (item) {
        res.json(item);
    } else {
        res.status(404).json({ error: "Article not found" });
    }
});

// Users List
app.get('/users/', (req, res) => {
    res.json(users);
});

// Auth Login
app.post('/auth/login', (req, res) => {
    const { username, password } = req.body;

    // Realistic login: admin/password OR user1/password
    if ((username === 'admin' || username === 'user1') && password === 'password') {
        res.json({
            accessToken: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock_access_token_" + Date.now(),
            refreshToken: "mock_refresh_token_" + Date.now()
        });
    } else if (!username || !password) {
        res.status(400).json({ error: "Username and password are required" });
    } else {
        res.status(401).json({ error: "Invalid credentials. Try admin/password" });
    }
});

// Auth Refresh
app.post('/auth/refresh', (req, res) => {
    const { refreshToken } = req.body;
    if (refreshToken && refreshToken.startsWith('mock_refresh_token_')) {
        res.json({
            accessToken: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refreshed_" + Date.now(),
            refreshToken: "mock_refresh_token_refreshed_" + Date.now()
        });
    } else {
        res.status(401).json({ error: "Invalid or expired refresh token" });
    }
});

app.listen(port, '0.0.0.0', () => {
    console.log('-------------------------------------------');
    console.log(`🚀 Mock Server ready at http://localhost:${port}`);
    console.log(`📱 For Android Physical Device: Use 'adb reverse tcp:3000 tcp:3000'`);
    console.log(`   and connect to http://localhost:${port}`);
    console.log('-------------------------------------------');
});
