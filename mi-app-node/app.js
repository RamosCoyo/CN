const express = require('express');
const app = express();

app.get('/', (req, res) => {
    res.status(200).send('Status 200 OK: Primera versión');
});

module.exports = app;