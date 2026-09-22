const request = require('supertest');
const app = require('./app');

test('La ruta raíz devuelve un Status 200', async () => {
    const response = await request(app).get('/');
    expect(response.statusCode).toBe(200);
});