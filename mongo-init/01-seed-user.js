db = db.getSiblingDB("codingexercise");

db.users.insertMany([
  {
    _id: "user-1",
    username: "demo-user",
    email: "demo.user@example.com",
    password: "password123",
  },
  {
    _id: "user-2",
    username: "tuser",
    email: "tuser@example.com",
    password: "tpass",
  },
]);
