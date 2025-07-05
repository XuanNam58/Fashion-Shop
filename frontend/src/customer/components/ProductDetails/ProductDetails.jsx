"use client";

import { useEffect, useState } from "react";
import { Radio, RadioGroup } from "@headlessui/react";
import { Box, Button, Grid, LinearProgress, Rating } from "@mui/material";
import ProductReviewCard from "./ProductReviewCard";
import { mens_vest } from "../../../Data/mens_vest";
import HomeSectionCard from "../HomeSectionCard/HomeSectionCard.jsx";
import { useNavigate, useParams } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { findProductById } from "../../../State/Product/Action.js";
import { addItemToCart } from "../../../State/Cart/Action.js";
import { toast } from "react-toastify";
import {
  getAverageRating,
  getProductRatings,
} from "../../../State/Rating/Action";
import { getProductReviews } from "../../../State/Review/Actions.js";

const product = {
  name: "Basic Tee 6-Pack",
  price: "$192",
  href: "#",
  breadcrumbs: [
    { id: 1, name: "Men", href: "#" },
    { id: 2, name: "Clothing", href: "#" },
  ],
  images: [
    {
      src: "https://tailwindui.com/plus/img/ecommerce-images/product-page-02-secondary-product-shot.jpg",
      alt: "Two each of gray, white, and black shirts laying flat.",
    },
    {
      src: "https://tailwindui.com/plus/img/ecommerce-images/product-page-02-tertiary-product-shot-01.jpg",
      alt: "Model wearing plain black basic tee.",
    },
    {
      src: "https://tailwindui.com/plus/img/ecommerce-images/product-page-02-tertiary-product-shot-02.jpg",
      alt: "Model wearing plain gray basic tee.",
    },
    {
      src: "https://tailwindui.com/plus/img/ecommerce-images/product-page-02-featured-product-shot.jpg",
      alt: "Model wearing plain white basic tee.",
    },
  ],
  colors: [
    { name: "White", class: "bg-white", selectedClass: "ring-gray-400" },
    { name: "Gray", class: "bg-gray-200", selectedClass: "ring-gray-400" },
    { name: "Black", class: "bg-gray-900", selectedClass: "ring-gray-900" },
  ],
  sizes: [
    { name: "S", inStock: true },
    { name: "M", inStock: true },
    { name: "L", inStock: true },
    { name: "XL", inStock: true },
  ],
  description:
    'The Basic Tee 6-Pack allows you to fully express your vibrant personality with three grayscale options. Feeling adventurous? Put on a heather gray tee. Want to be a trendsetter? Try our exclusive colorway: "Black". Need to add an extra pop of color to your outfit? Our white tee has you covered.',
  highlights: [
    "Hand cut and sewn locally",
    "Dyed with our proprietary colors",
    "Pre-washed & pre-shrunk",
    "Ultra-soft 100% cotton",
  ],
  details:
    'The 6-Pack includes two black, two white, and two heather gray Basic Tees. Sign up for our subscription service and be the first to get new, exciting colors, like our upcoming "Charcoal Gray" limited release.',
};

function classNames(...classes) {
  return classes.filter(Boolean).join(" ");
}

export default function ProductDetails() {
  const [selectedSize, setSelectedSize] = useState("");
  const [isAnimating, setIsAnimating] = useState(false);
  const navigate = useNavigate();
  const params = useParams();
  const dispatch = useDispatch();
  const { products } = useSelector((store) => store);
  const reviews = useSelector((store) => store.review);
  const ratings = useSelector((store) => store.rating);
  const [error, setError] = useState("");
  const user = useSelector((store) => store.auth.user);

  // Đảm bảo reviews.reviews là mảng
  // const reviewList = Array.isArray(reviews.reviews) ? reviews.reviews : [];


  const handleSizeChange = (size) => {
    setIsAnimating(true);
    setSelectedSize(size);

    // Reset animation after a short delay
    setTimeout(() => {
      setIsAnimating(false);
    }, 200);
  };

  const handleAddToCart = () => {
    if (!selectedSize) {
      toast.error("Please select a size");
      return;
    }

    if (!user) {
      toast.error("Please login to add product to cart");
      return;
    }

    const data = { productId: params.productId, size: selectedSize.name };
    dispatch(addItemToCart(data));
    toast.success("Added product successfully");
    // navigate("/cart")
  };

  useEffect(() => {
    dispatch(findProductById({ productId: params.productId }));
    dispatch(getAverageRating(params.productId));
    dispatch(getProductReviews({ productId: params.productId }));
    dispatch(getProductRatings({ productId: params.productId }));
  }, [params.productId]);

  // Combine reviews and ratings
  const combinedReviews = reviews?.reviews.map((review) => ({
    ...review,
    rating:
      (Array.isArray(ratings.ratings)
        ? ratings.ratings
        : []
      ).find(
        (r) =>
          r.user.id === review.user.id && r.productId === review.productId
      )?.rating || 0,
  }));

  return (
    <div className="bg-white lg:px-20">
      <div className="pt-6">
        <nav aria-label="Breadcrumb">
          <ol
            role="list"
            className="mx-auto flex max-w-2xl items-center space-x-2 px-4 sm:px-6 lg:max-w-7xl lg:px-8"
          >
            {product.breadcrumbs.map((breadcrumb) => (
              <li key={breadcrumb.id}>
                <div className="flex items-center">
                  <a
                    href={breadcrumb.href}
                    className="mr-2 text-sm font-medium text-gray-900"
                  >
                    {breadcrumb.name}
                  </a>
                  <svg
                    fill="currentColor"
                    width={16}
                    height={20}
                    viewBox="0 0 16 20"
                    aria-hidden="true"
                    className="h-5 w-4 text-gray-300"
                  >
                    <path d="M5.697 4.34L8.98 16.532h1.327L7.025 4.341H5.697z" />
                  </svg>
                </div>
              </li>
            ))}
            <li className="text-sm">
              <a
                href={product.href}
                aria-current="page"
                className="font-medium text-gray-500 hover:text-gray-600"
              >
                {products.product?.title}
              </a>
            </li>
          </ol>
        </nav>

        <section className="grid grid-cols-1 lg:grid-cols-2 gap-x-8 gap-y-10 px-4 pt-10">
          {/* Image gallery */}
          <div className="flex flex-col items-center">
            <div className="overflow-hidden rounded-lg max-w-[30rem] max-h-[35rem]">
              <img
                alt={product.images[0].alt}
                src={products.product?.imageUrl || "/placeholder.svg"}
                className="hidden size-full rounded-lg object-cover lg:block"
              />
            </div>
            <div className="flex flex-wrap space-x-5 justify-center">
              {product.images.map((item, index) => (
                <div
                  key={index}
                  className="aspect-h-2 aspect-w-3 overflow-hidden rounded-lg max-w-[5rem] max-h-[5rem] mt-4"
                >
                  <img
                    alt={item.alt}
                    src={item.src || "/placeholder.svg"}
                    className="aspect-[3/2] w-full rounded-lg object-cover"
                  />
                </div>
              ))}
            </div>
          </div>

          {/* Product info */}
          <div className="lg:col-span-1 maxt-auto max-w-2xl px-4 pb-16 sm:px-6 lg:max-w-7xl lg:px-8 lg:pb-24">
            <div className="lg:col-span-2">
              <h1 className="text-lg lg:text-xl font-semibold text-gray-900">
                {products.product?.brand}
              </h1>
              <h1 className="text-lg lg:text-xl text-gray-900 opacity-60 pt-1">
                {products.product?.title}
              </h1>
            </div>

            {/* Options */}
            <div className="mt-4 lg:row-span-3 lg:mt-0">
              <h2 className="sr-only">Product information</h2>

              <div className="flex space-x-5 items-center text-lg lg:text-xl text-gray-900 mt-6">
                <p className="font-semibold">
                  {products.product?.discountedPrice}
                </p>
                <p className="opacity-50 line-through">
                  {products.product?.price}
                </p>
                <p className="text-green-600 font-semibold">
                  {products.product?.discountPercent}% off
                </p>
              </div>

              {/* Reviews */}
              <div className="mt-6">
                <div className="flex items-center space-x-3">
                  <Rating name="read-only" value={ratings.averageRating} precision={0.5} readOnly />
                  <p className="opacity-50 text-sm">{ratings.totalRatings || 0} Ratings</p>
                  <p className="ml-3 text-sm font-medium text-indigo-600 hover:text-indigo-500">
                    {reviews.reviews.length} Reviews
                  </p>
                </div>
              </div>

              <form className="mt-10">
                {/* Enhanced Sizes with animations */}
                <div className="mt-10">
                  <div className="flex items-center justify-between">
                    <h3 className="text-sm font-medium text-gray-900">Size</h3>
                    {selectedSize && (
                      <span className="text-sm text-indigo-600 font-medium animate-fade-in">
                        Selected: {selectedSize.name}
                      </span>
                    )}
                  </div>

                  <fieldset aria-label="Choose a size" className="mt-4">
                    <RadioGroup
                      value={selectedSize}
                      onChange={handleSizeChange}
                      className="grid grid-cols-4 gap-4 sm:grid-cols-8 lg:grid-cols-4"
                    >
                      {products.product?.sizes.map((size) => (
                        <Radio
                          key={size.name}
                          value={size}
                          disabled={size.quantity === 0}
                          className={({ checked, disabled }) =>
                            classNames(
                              // Base styles
                              "group relative flex items-center justify-center rounded-lg border px-4 py-3 text-sm font-medium uppercase focus:outline-none transition-all duration-300 ease-in-out transform",
                              // Available vs disabled states
                              !disabled
                                ? "cursor-pointer bg-white text-gray-900 shadow-sm border-gray-300"
                                : "cursor-not-allowed bg-gray-50 text-gray-200 border-gray-200",
                              // Hover effects for available sizes
                              !disabled &&
                                "hover:bg-gray-50 hover:border-gray-400 hover:scale-105 hover:shadow-md",
                              // Selected state
                              checked &&
                                !disabled &&
                                "bg-indigo-50 border-indigo-500 text-indigo-700 ring-2 ring-indigo-500 ring-offset-2 scale-105 shadow-lg",
                              // Animation when selecting
                              checked && isAnimating && "animate-pulse",
                              // Focus state
                              "focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                            )
                          }
                        >
                          {({ checked }) => (
                            <>
                              <span
                                className={classNames(
                                  "transition-all duration-200",
                                  checked ? "font-bold" : "font-medium"
                                )}
                              >
                                {size.name}
                              </span>
                              {size.quantity === 0 && (
                                <span className="text-xs text-red-500 ml-2 animate-fade-in">
                                  (Out of stock)
                                </span>
                              )}
                              {/* Selected indicator */}
                              {checked && (
                                <div className="absolute -top-1 -right-1 w-3 h-3 bg-indigo-500 rounded-full animate-bounce">
                                  <div className="w-full h-full bg-white rounded-full scale-50"></div>
                                </div>
                              )}
                            </>
                          )}
                        </Radio>
                      ))}
                    </RadioGroup>
                  </fieldset>
                </div>

                <Button
                  onClick={handleAddToCart}
                  variant="contained"
                  className={classNames(
                    "transition-all duration-300 ease-in-out transform",
                    selectedSize
                      ? "hover:scale-105 hover:shadow-lg"
                      : "opacity-75"
                  )}
                  sx={{
                    px: "2.5rem",
                    py: "1rem",
                    bgcolor: "#9155fd",
                    marginTop: "1rem",
                    "&:hover": {
                      bgcolor: "#7c3aed",
                    },
                  }}
                >
                  Add to Cart
                </Button>

                {error && (
                  <div className="text-red-500 mt-2 animate-fade-in">
                    {error}
                  </div>
                )}
              </form>
            </div>

            <div className="py-10 lg:col-span-2 lg:col-start-1 lg:border-r lg:border-gray-200 lg:pb-16 lg:pr-8 lg:pt-6">
              {/* Description and details */}
              <div>
                <h3 className="sr-only">Description</h3>
                <div className="space-y-6">
                  <p className="text-base text-gray-900">
                    {products?.product?.description}
                  </p>
                </div>
              </div>

              <div className="mt-10">
                <h3 className="text-sm font-medium text-gray-900">
                  Highlights
                </h3>
                <div className="mt-4">
                  <ul role="list" className="list-disc space-y-2 pl-4 text-sm">
                    {product.highlights.map((highlight) => (
                      <li key={highlight} className="text-gray-400">
                        <span className="text-gray-600">{highlight}</span>
                      </li>
                    ))}
                  </ul>
                </div>
              </div>

              <div className="mt-10">
                <h2 className="text-sm font-medium text-gray-900">Details</h2>
                <div className="mt-4 space-y-6">
                  <p className="text-sm text-gray-600">{product.details}</p>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* rating and reviews */}
        <section>
          <h1 className="font-semibold text-lg pb-4">
            Recent Reviews & Rating
          </h1>
          <div className="border p-5">
            <Grid container spacing={7}>
              <Grid item xs={7}>
                <div className="space-y-5">
                  {combinedReviews.length > 0 ? (
                    combinedReviews.map((review, index) => (
                      <ProductReviewCard key={index} review={review} />
                    ))
                  ) : (
                    <p>No reviews yet.</p>
                  )}
                </div>
              </Grid>
              <Grid item xs={5}>
                <h1 className="text-xl font-semibold pb-2">Product Ratings</h1>
                <div className="flex items-center space-x-3">
                  <Rating
                    value={ratings.averageRating || 0}
                    precision={0.5}
                    readOnly
                  />
                  <p className="opacity-60">
                    {ratings.totalRatings || 0} Ratings
                  </p>
                </div>
                <Box className="mt-5 space-y-3">
                  <Grid container alignItems={"center"} gap={2}>
                    <Grid item xs={2}>
                      <p>Excellent</p>
                    </Grid>
                    <Grid item xs={7}>
                      <LinearProgress
                        sx={{ bgcolor: "#d0d0d0", borderRadius: 4, height: 7 }}
                        variant="determinate"
                        value={40}
                        color="success"
                      />
                    </Grid>
                  </Grid>
                  <Grid container alignItems={"center"} gap={2}>
                    <Grid item xs={2}>
                      <p>Very Good</p>
                    </Grid>
                    <Grid item xs={7}>
                      <LinearProgress
                        sx={{ bgcolor: "#d0d0d0", borderRadius: 4, height: 7 }}
                        variant="determinate"
                        value={30}
                        color="success"
                      />
                    </Grid>
                  </Grid>
                  <Grid container alignItems={"center"} gap={2}>
                    <Grid item xs={2}>
                      <p>Good</p>
                    </Grid>
                    <Grid item xs={7}>
                      <LinearProgress
                        sx={{ bgcolor: "#d0d0d0", borderRadius: 4, height: 7 }}
                        variant="determinate"
                        value={25}
                        color="info"
                      />
                    </Grid>
                  </Grid>
                  <Grid container alignItems={"center"} gap={2}>
                    <Grid item xs={2}>
                      <p>Average</p>
                    </Grid>
                    <Grid item xs={7}>
                      <LinearProgress
                        sx={{ bgcolor: "#d0d0d0", borderRadius: 4, height: 7 }}
                        variant="determinate"
                        value={20}
                        color="warning"
                      />
                    </Grid>
                  </Grid>
                  <Grid container alignItems={"center"} gap={2}>
                    <Grid item xs={2}>
                      <p>Poor</p>
                    </Grid>
                    <Grid item xs={7}>
                      <LinearProgress
                        sx={{ bgcolor: "#d0d0d0", borderRadius: 4, height: 7 }}
                        variant="determinate"
                        value={10}
                        color="error"
                      />
                    </Grid>
                  </Grid>
                </Box>
              </Grid>
            </Grid>
          </div>
        </section>

        {/* similar products */}
        <section className="pt-10">
          <h1 className="py-5 text-xl font-bold">Similar Products</h1>
          <div className="flex flex-wrap space-x-5">
            {mens_vest.map((item, index) => (
              <HomeSectionCard key={index} product={item} />
            ))}
          </div>
        </section>
      </div>

      <style jsx>{`
        @keyframes fade-in {
          from {
            opacity: 0;
            transform: translateY(-10px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }

        .animate-fade-in {
          animation: fade-in 0.3s ease-out;
        }
      `}</style>
    </div>
  );
}
