db = db.getSiblingDB("codingexercise");

db.packages.insertMany([
  {
    _id: "pkg-starter-001",
    name: "Start Pack",
    description: "Essential items for new adventurers",
    productQuantities: {
      "7Hv0hA2nmci7": 1, // Knife
      "500R5EHvNlNB": 5, // Gold Coin
    },
    ownerUsername: "tuser",
  },
  {
    _id: "pkg-adept-002",
    name: "Adept Pack",
    description: "Gear for experienced warriors",
    productQuantities: {
      "7dgX6XzU3Wds": 1, // Sword
      VqKb4tyj9V6i: 1, // Shield
      IP3cv7TcZhQn: 3, // Platinum Coin
    },
    ownerUsername: "tuser",
  },
  {
    _id: "pkg-artisan-003",
    name: "Artisan Pack",
    description: "Premium collection for master craftsmen",
    productQuantities: {
      DXSQpv6XVeJm: 1, // Helmet
      PKM5pGAh9yGm: 1, // Axe
      IJOHGYkY2CYq: 1, // Bow
      IP3cv7TcZhQn: 10, // Platinum Coin
    },
    ownerUsername: "tuser",
  },
]);
