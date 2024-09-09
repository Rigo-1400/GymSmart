const express = require('express');
const app = express();
const PORT = 3000 || process.env.PORT;

// Middleware
app.use(express.json());

// Default Route
app.get('/', (req, res) => {
    res.send({message: "This is some json"});
})

// Listen on a part
app.listen(PORT);
