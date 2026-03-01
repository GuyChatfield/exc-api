db = db.getSiblingDB("codingexercise");

const products = [
  {
    _id: "VqKb4tyj9V6i",
    id: "VqKb4tyj9V6i",
    name: "Shield",
    usdPrice: 1149,
  },
  {
    _id: "DXSQpv6XVeJm",
    id: "DXSQpv6XVeJm",
    name: "Helmet",
    usdPrice: 999,
  },
  {
    _id: "7dgX6XzU3Wds",
    id: "7dgX6XzU3Wds",
    name: "Sword",
    usdPrice: 899,
  },
  {
    _id: "PKM5pGAh9yGm",
    id: "PKM5pGAh9yGm",
    name: "Axe",
    usdPrice: 799,
  },
  {
    _id: "7Hv0hA2nmci7",
    id: "7Hv0hA2nmci7",
    name: "Knife",
    usdPrice: 349,
  },
  {
    _id: "500R5EHvNlNB",
    id: "500R5EHvNlNB",
    name: "Gold Coin",
    usdPrice: 249,
  },
  {
    _id: "IP3cv7TcZhQn",
    id: "IP3cv7TcZhQn",
    name: "Platinum Coin",
    usdPrice: 399,
  },
  {
    _id: "IJOHGYkY2CYq",
    id: "IJOHGYkY2CYq",
    name: "Bow",
    usdPrice: 649,
  },
  {
    _id: "8anPsR2jbfNW",
    id: "8anPsR2jbfNW",
    name: "Silver Coin",
    usdPrice: 50,
  },
];

products.forEach((product) => {
  db.products.replaceOne({ _id: product._id }, product, { upsert: true });
});
