export const navigation = {
  categories: [
    {
      id: "women",
      name: "Women",
      featured: [
        {
          name: "New Arrivals",
          href: "/",
          imageSrc:
            "https://png.pngtree.com/png-vector/20240312/ourlarge/pngtree-front-and-back-model-wearing-black-t-shirt-png-image_11932535.png",
          imageAlt:
            "Models standing back to back",
        },
        {
          name: "Basic Tees",
          href: "/",
          imageSrc:
            "https://cdn.pixabay.com/photo/2024/08/21/16/12/woman-8986665_640.jpg",
          imageAlt:
            "Woman model",
        },
      ],
      sections: [
        {
          id: "clothing",
          name: "Clothing",
          items: [
            { name: "Tops", id: "top", href: `{women/clothing/tops}` },
            { name: "Dress", id: "dress", href: "#" },
            { name: "Women Jeans", id: "women_jeans" },
            { name: "Lengha Choli", id: "lengha_choli" },
            { name: "Sweaters", id: "sweater" },
            { name: "T-Shirts", id: "t-shirt" },
            { name: "Jackets", id: "jacket" },
          ],
        },
        {
          id: "accessories",
          name: "Accessories",
          items: [
            { name: "Watches", id: "watch" },
            { name: "Wallets", id: "wallet" },
            { name: "Bags", id: "bag" },
            { name: "Sunglasses", id: "sunglasse" },
            { name: "Hats", id: "hat" },
            { name: "Belts", id: "belt" },
          ],
        },
        {
          id: "brands",
          name: "Brands",
          items: [
            { name: "Full Nelson", id: "#" },
            { name: "My Way", id: "#" },
            { name: "Re-Arranged", id: "#" },
            { name: "Counterfeit", id: "#" },
            { name: "Significant Other", id: "#" },
          ],
        },
      ],
    },
    {
      id: "men",
      name: "Men",
      featured: [
        {
          name: "New Arrivals",
          id: "#",
          imageSrc:
            "https://cdn.pixabay.com/photo/2016/11/29/01/34/man-1866574_960_720.jpg",
          imageAlt:
            "Man model",
        },
        {
          name: "Artwork Tees",
          id: "#",
          imageSrc:
            "https://tse1.mm.bing.net/th/id/OIP.6wwB7fiseTMs0quOmLK6OwHaLH?rs=1&pid=ImgDetMain&o=7&rm=3",
          imageAlt:
            "Man model",
        },
      ],
      sections: [
        {
          id: "clothing",
          name: "Clothing",
          items: [
            { name: "Vest", id: "vest" },
            { name: "Shirt", id: "shirt" },
            { name: "Men Jeans", id: "men_jeans" },
            { name: "Sweaters", id: "#" },
            { name: "T-Shirts", id: "t-shirt" },
            { name: "Jackets", id: "#" },
            { name: "Activewear", id: "#" },
          ],
        },
        {
          id: "accessories",
          name: "Accessories",
          items: [
            { name: "Watches", id: "#" },
            { name: "Wallets", id: "#" },
            { name: "Bags", id: "#" },
            { name: "Sunglasses", id: "#" },
            { name: "Hats", id: "#" },
            { name: "Belts", id: "#" },
          ],
        },
        {
          id: "brands",
          name: "Brands",
          items: [
            { name: "Re-Arranged", id: "#" },
            { name: "Counterfeit", id: "#" },
            { name: "Full Nelson", id: "#" },
            { name: "My Way", id: "#" },
          ],
        },
      ],
    },
  ],
  pages: [
    { name: "Company", id: "/" },
    { name: "Stores", id: "/" },
  ],
};
