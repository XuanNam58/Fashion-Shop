export const color = [
  "white",
  "Black",
  "Red",
  "marun",
  "Being",
  "Pink",
  "Green",
  "Yellow",
];

export const filters = [
  {
    id: "color",
    name: "Color",
    options: [
      { value: "white", label: "White" },
      { value: "beige", label: "Beige" },
      { value: "gray", label: "Gray" },
      { value: "blue", label: "Blue" },
      { value: "black", label: "Black" },
      { value: "red", label: "Red" },
      { value: "brown", label: "Brown" },
      { value: "green", label: "Green" },
      { value: "purple", label: "Purple" },
      { value: "pink", label: "Pink" },
      { value: "yellow", label: "Yellow" },
    ],
  },

  {
    id: "size",
    name: "Size",
    options: [
      { value: "S", label: "S" },
      { value: "M", label: "M" },
      { value: "L", label: "L" },
      { value: "XL", label: "XL" },
    ],
  },
];

export const singleFilter = [
  {
    id: "price",
    name: "Price",
    options: [
      { value: "1000-5000", label: "1000 To 5000" },
      { value: "5000-10000", label: "5000 To 10000" },
      { value: "10000-20000", label: "10000 To 20000" },
      { value: "20000-30000", label: "20000 To 30000" },
    ],
  },

  {
    id: "discount",
    name: "Discount Range",
    options: [
      {
        value: "10",
        label: "10% And Above",
      },

      { value: "20", label: "20% And Above" },
      { value: "30", label: "30% And Above" },
      { value: "40", label: "40% And Above" },
      { value: "50", label: "50% And Above" },
      { value: "60", label: "60% And Above" },
      { value: "70", label: "70% And Above" },
      { value: "80", label: "80% And Above" },
    ],
  },

  {
    id: "availability",
    name: "Availability",
    options: [
      { value: "in_stock", label: "In Stock" },
      { value: "out_of_stock", label: "Out Of Stock" },
    ],
  },
];
