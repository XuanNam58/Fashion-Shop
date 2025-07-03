"use client";

import { useEffect, useState } from "react";
import {
  Dialog,
  DialogBackdrop,
  DialogPanel,
  Disclosure,
  DisclosureButton,
  DisclosurePanel,
  Menu,
  MenuButton,
  MenuItem,
  MenuItems,
} from "@headlessui/react";
import { XMarkIcon, MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import {
  ChevronDownIcon,
  FunnelIcon,
  MinusIcon,
  PlusIcon,
  Squares2X2Icon,
} from "@heroicons/react/20/solid";
import ProductCard from "./ProductCard";
import { filters, singleFilter } from "./FilterData";
import {
  FormControl,
  FormControlLabel,
  FormLabel,
  Radio,
  RadioGroup,
} from "@mui/material";
import FilterListIcon from "@mui/icons-material/FilterList";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { findProducts, searchProducts } from "../../../State/Product/Action";
import Pagination from "@mui/material/Pagination";

const sortOptions = [
  { name: "Price: Low to High", value: "price,asc", current: false },
  { name: "Price: High to Low", value: "price,desc", current: false },
  { name: "Newest First", value: "createdAt,desc", current: false },
  { name: "Oldest First", value: "createdAt,asc", current: false },
  { name: "Name A-Z", value: "title,asc", current: false },
  { name: "Name Z-A", value: "title,desc", current: false },
];

function classNames(...classes) {
  return classes.filter(Boolean).join(" ");
}

export default function Product() {
  const [mobileFiltersOpen, setMobileFiltersOpen] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();
  const param = useParams();
  const dispatch = useDispatch();
  const { products } = useSelector((store) => store);
  /* useSelector: Đây là một hook được cung cấp bởi react-redux, 
  cho phép bạn lấy state từ Redux store trong các component React.

    store => store: Hàm này trả về toàn bộ state của Redux store. 
  Trong trường hợp này, product được lấy ra từ state. */

  const decodedQueryString = decodeURIComponent(location.search);
  const searchParams = new URLSearchParams(decodedQueryString);
  const colorValue = searchParams.get("color");
  const sizeValue = searchParams.get("size");
  const priceValue = searchParams.get("price");
  const discount = searchParams.get("discount");
  const sortValue = searchParams.get("sort");
  const pageNumber = searchParams.get("page") || 1;
  const stock = searchParams.get("stock");
  const searchQuery = searchParams.get("q");

  // Check if we're in search mode
  const isSearchMode = !!searchQuery;
  
  // Check if we're on search route (no category params)
  const isSearchRoute = !param.levelOne && !param.levelTwo && !param.levelThree;
  
  // Check if we're showing all products (search route without query)
  const isAllProductsMode = isSearchRoute && !isSearchMode;
  
  // Get page title based on mode
  const getPageTitle = () => {
    if (isSearchMode) {
      return "Search results";
    } else if (isAllProductsMode) {
      return "All products";
    } else {
      return param.levelThree ? param.levelThree.charAt(0).toUpperCase() + param.levelThree.slice(1) : "Products";
    }
  };

  // Get subtitle based on mode
  const getSubtitle = () => {
    if (isSearchMode) {
      return `Found ${products.products?.totalElements || 0} products for "${searchQuery}"`;
    } else if (isAllProductsMode) {
      return `Browse our complete collection of ${products.products?.totalElements || 0} products`;
    }
    return null;
  };

  const handlePaginationChange = (value) => {
    const searchParams = new URLSearchParams(location.search);
    searchParams.set("page", value);
    const query = searchParams.toString();
    navigate({ search: `?${query}` });
  };

  const handleFilter = (value, sectionId) => {
    const searchParams = new URLSearchParams(location.search);
    let filterValue = searchParams.getAll(sectionId);

    if (filterValue.length > 0 && filterValue[0].split(",").includes(value)) {
      filterValue = filterValue[0].split(",").filter((item) => item !== value);

      if (filterValue.length === 0) {
        searchParams.delete(sectionId);
      }
    } else {
      filterValue.push(value);
    }

    if (filterValue.length > 0) {
      searchParams.set(sectionId, filterValue.join(","));
    }
    const query = searchParams.toString();
    navigate({ search: `?${query}` });
  };

  const handleRadioFilterChange = (e, sectionId) => {
    const searchParams = new URLSearchParams(location.search);
    searchParams.set(sectionId, e.target.value);
    const query = searchParams.toString();
    navigate({ search: `?${query}` });
  };

  const handleSortChange = (sortValue) => {
    const searchParams = new URLSearchParams(location.search);
    searchParams.set("sort", sortValue);
    searchParams.set("page", "1"); // Reset to first page when sorting
    const query = searchParams.toString();
    navigate({ search: `?${query}` });
  };

  useEffect(() => {
    if (isSearchMode) {
      // Search mode - use searchProducts action with query
      const data = {
        q: searchQuery,
        page: pageNumber - 1,
        size: 12,
        sort: sortValue || "createdAt,desc",
      };
      dispatch(searchProducts(data));
    } else if (isAllProductsMode) {
      // All products mode - use findProducts action without category filter
      const data = {
        category: "", // Empty category to get all products
        colors: colorValue || [],
        sizes: sizeValue || [],
        minPrice: 0,
        maxPrice: 20000000,
        minDiscount: discount || 0,
        sort: sortValue || "createdAt,desc",
        stock: stock,
        pageNumber: pageNumber - 1,
        pageSize: 12,
      };
      dispatch(findProducts(data));
    } else {
      // Category mode - use findProducts action with category
      const [minPrice, maxPrice] =
        priceValue === null ? [0, 20000000] : priceValue.split("-").map(Number);
      const data = {
        category: param.levelThree,
        colors: colorValue || [],
        sizes: sizeValue || [],
        minPrice,
        maxPrice,
        minDiscount: discount || 0,
        sort: sortValue || "price_low",
        stock: stock,
        pageNumber: pageNumber - 1,
        pageSize: 10,
      };
      dispatch(findProducts(data));
    }
  }, [
    param.levelThree,
    colorValue,
    sizeValue,
    priceValue,
    discount,
    sortValue,
    pageNumber,
    stock,
    searchQuery,
    isSearchMode,
    isAllProductsMode,
    dispatch,
  ]);

  // Get current sort option
  const currentSort = sortOptions.find(option => option.value === sortValue) || sortOptions[0];

  return (
    <div className="bg-white">
      <div>
        {/* Mobile filter dialog */}
        <Dialog
          open={mobileFiltersOpen}
          onClose={setMobileFiltersOpen}
          className="relative z-40 lg:hidden"
        >
          <DialogBackdrop
            transition
            className="fixed inset-0 bg-black/25 transition-opacity duration-300 ease-linear data-[closed]:opacity-0"
          />

          <div className="fixed inset-0 z-40 flex">
            <DialogPanel
              transition
              className="relative ml-auto flex size-full max-w-xs transform flex-col overflow-y-auto bg-white py-4 pb-12 shadow-xl transition duration-300 ease-in-out data-[closed]:translate-x-full"
            >
              <div className="flex items-center justify-between px-4">
                <h2 className="text-lg font-medium text-gray-900">Filters</h2>
                <button
                  type="button"
                  onClick={() => setMobileFiltersOpen(false)}
                  className="-mr-2 flex size-10 items-center justify-center rounded-md bg-white p-2 text-gray-400"
                >
                  <span className="sr-only">Close menu</span>
                  <XMarkIcon aria-hidden="true" className="size-6" />
                </button>
              </div>

              {/* Filters - only show in category mode */}
              {!isSearchMode && !isAllProductsMode && (
                <form className="mt-4 border-t border-gray-200">
                  {filters.map((section) => (
                    <Disclosure
                      key={section.id}
                      as="div"
                      className="border-t border-gray-200 px-4 py-6"
                    >
                      <h3 className="-mx-2 -my-3 flow-root">
                        <DisclosureButton className="group flex w-full items-center justify-between bg-white px-2 py-3 text-gray-400 hover:text-gray-500">
                          <span className="font-medium text-gray-900">
                            {section.name}
                          </span>
                          <span className="ml-6 flex items-center">
                            <PlusIcon
                              aria-hidden="true"
                              className="size-5 group-data-[open]:hidden"
                            />
                            <MinusIcon
                              aria-hidden="true"
                              className="size-5 group-[&:not([data-open])]:hidden"
                            />
                          </span>
                        </DisclosureButton>
                      </h3>
                      <DisclosurePanel className="pt-6">
                        <div className="space-y-6">
                          {section.options.map((option, optionIdx) => (
                            <div key={option.value} className="flex gap-3">
                              <div className="flex h-5 shrink-0 items-center">
                                <div className="group grid size-4 grid-cols-1">
                                  <input
                                    defaultValue={option.value}
                                    id={`filter-mobile-${section.id}-${optionIdx}`}
                                    name={`${section.id}[]`}
                                    type="checkbox"
                                    className="col-start-1 row-start-1 appearance-none rounded border border-gray-300 bg-white checked:border-indigo-600 checked:bg-indigo-600 indeterminate:border-indigo-600 indeterminate:bg-indigo-600 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 disabled:border-gray-300 disabled:bg-gray-100 disabled:checked:bg-gray-100 forced-colors:appearance-auto"
                                  />
                                  <svg
                                    fill="none"
                                    viewBox="0 0 14 14"
                                    className="pointer-events-none col-start-1 row-start-1 size-3.5 self-center justify-self-center stroke-white group-has-[:disabled]:stroke-gray-950/25"
                                  >
                                    <path
                                      d="M3 8L6 11L11 3.5"
                                      strokeWidth={2}
                                      strokeLinecap="round"
                                      strokeLinejoin="round"
                                      className="opacity-0 group-has-[:checked]:opacity-100"
                                    />
                                    <path
                                      d="M3 7H11"
                                      strokeWidth={2}
                                      strokeLinecap="round"
                                      strokeLinejoin="round"
                                      className="opacity-0 group-has-[:indeterminate]:opacity-100"
                                    />
                                  </svg>
                                </div>
                              </div>
                              <label
                                htmlFor={`filter-mobile-${section.id}-${optionIdx}`}
                                className="min-w-0 flex-1 text-gray-500"
                              >
                                {option.label}
                              </label>
                            </div>
                          ))}
                        </div>
                      </DisclosurePanel>
                    </Disclosure>
                  ))}
                </form>
              )}
            </DialogPanel>
          </div>
        </Dialog>

        <main className="mx-auto px-4 sm:px-6 lg:px-20">
          <div className="flex items-baseline justify-between border-b border-gray-200 pb-6 pt-24">
            <div className="flex items-center space-x-3">
              {isSearchMode && (
                <MagnifyingGlassIcon className="h-8 w-8 text-indigo-600" />
              )}
              <h1 className="text-4xl font-bold tracking-tight text-gray-900">
                {getPageTitle()}
              </h1>
              {getSubtitle() && (
                <span className="block text-lg font-normal text-gray-600 mt-1">
                  {getSubtitle()}
                </span>
              )}
            </div>

            <div className="flex items-center">
              <Menu as="div" className="relative inline-block text-left">
                <div>
                  <MenuButton className="group inline-flex justify-center text-sm font-medium text-gray-700 hover:text-gray-900">
                    Sort
                    <ChevronDownIcon
                      aria-hidden="true"
                      className="-mr-1 ml-1 size-5 shrink-0 text-gray-400 group-hover:text-gray-500"
                    />
                  </MenuButton>
                </div>

                <MenuItems
                  transition
                  className="absolute right-0 z-10 mt-2 w-40 origin-top-right rounded-md bg-white shadow-2xl ring-1 ring-black/5 transition focus:outline-none data-[closed]:scale-95 data-[closed]:transform data-[closed]:opacity-0 data-[enter]:duration-100 data-[leave]:duration-75 data-[enter]:ease-out data-[leave]:ease-in"
                >
                  <div className="py-1">
                    {sortOptions.map((option) => (
                      <MenuItem key={option.name}>
                        <button
                          onClick={() => handleSortChange(option.value)}
                          className={classNames(
                            currentSort.value === option.value
                              ? "font-medium text-gray-900"
                              : "text-gray-500",
                            "block w-full text-left px-4 py-2 text-sm data-[focus]:bg-gray-100 data-[focus]:outline-none"
                          )}
                        >
                          {option.name}
                        </button>
                      </MenuItem>
                    ))}
                  </div>
                </MenuItems>
              </Menu>

              <button
                type="button"
                className="-m-2 ml-5 p-2 text-gray-400 hover:text-gray-500 sm:ml-7"
              >
                <span className="sr-only">View grid</span>
                <Squares2X2Icon aria-hidden="true" className="size-5" />
              </button>
              {!isSearchMode && !isAllProductsMode && (
                <button
                  type="button"
                  onClick={() => setMobileFiltersOpen(true)}
                  className="-m-2 ml-4 p-2 text-gray-400 hover:text-gray-500 sm:ml-6 lg:hidden"
                >
                  <span className="sr-only">Filters</span>
                  <FunnelIcon aria-hidden="true" className="size-5" />
                </button>
              )}
            </div>
          </div>

          <section aria-labelledby="products-heading" className="pb-24 pt-6">
            <h2 id="products-heading" className="sr-only">
              Products
            </h2>

            <div className="grid grid-cols-1 gap-x-8 gap-y-10 lg:grid-cols-5">
              {/* Filters sidebar - only show in category mode */}
              {!isSearchMode && !isAllProductsMode && (
                <div>
                  <div className="py-10 flex items-center justify-between">
                    <h1 className="text-lg opacity-50 font-bold">Filters</h1>
                    <FilterListIcon />
                  </div>
                  <form className="hidden lg:block">
                    {filters.map((section) => (
                      <Disclosure
                        key={section.id}
                        as="div"
                        className="border-b border-gray-200 py-6"
                      >
                        <h3 className="-my-3 flow-root">
                          <DisclosureButton className="group flex w-full items-center justify-between bg-white py-3 text-sm text-gray-400 hover:text-gray-500">
                            <span className="font-medium text-gray-900">
                              {section.name}
                            </span>
                            <span className="ml-6 flex items-center">
                              <PlusIcon
                                aria-hidden="true"
                                className="size-5 group-data-[open]:hidden"
                              />
                              <MinusIcon
                                aria-hidden="true"
                                className="size-5 group-[&:not([data-open])]:hidden"
                              />
                            </span>
                          </DisclosureButton>
                        </h3>
                        <DisclosurePanel className="pt-6">
                          <div className="space-y-4">
                            {section.options.map((option, optionIdx) => (
                              <div key={option.value} className="flex gap-3">
                                <div className="flex h-5 shrink-0 items-center">
                                  <div className="group grid size-4 grid-cols-1">
                                    <input
                                      onChange={() =>
                                        handleFilter(option.value, section.id)
                                      }
                                      defaultValue={option.value}
                                      defaultChecked={option.checked}
                                      id={`filter-${section.id}-${optionIdx}`}
                                      name={`${section.id}[]`}
                                      type="checkbox"
                                      className="col-start-1 row-start-1 appearance-none rounded border border-gray-300 bg-white checked:border-indigo-600 checked:bg-indigo-600 indeterminate:border-indigo-600 indeterminate:bg-indigo-600 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 disabled:border-gray-300 disabled:bg-gray-100 disabled:checked:bg-gray-100 forced-colors:appearance-auto"
                                    />
                                    <svg
                                      fill="none"
                                      viewBox="0 0 14 14"
                                      className="pointer-events-none col-start-1 row-start-1 size-3.5 self-center justify-self-center stroke-white group-has-[:disabled]:stroke-gray-950/25"
                                    >
                                      <path
                                        d="M3 8L6 11L11 3.5"
                                        strokeWidth={2}
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        className="opacity-0 group-has-[:checked]:opacity-100"
                                      />
                                      <path
                                        d="M3 7H11"
                                        strokeWidth={2}
                                        strokeLinecap="round"
                                        strokeLinejoin="round"
                                        className="opacity-0 group-has-[:indeterminate]:opacity-100"
                                      />
                                    </svg>
                                  </div>
                                </div>
                                <label
                                  htmlFor={`filter-${section.id}-${optionIdx}`}
                                  className="text-sm text-gray-600"
                                >
                                  {option.label}
                                </label>
                              </div>
                            ))}
                          </div>
                        </DisclosurePanel>
                      </Disclosure>
                    ))}

                    {singleFilter.map((section) => (
                      <Disclosure
                        key={section.id}
                        as="div"
                        className="border-b border-gray-200 py-6"
                      >
                        <>
                          <h3 className="-my-3 flow-root">
                            <DisclosureButton className="group flex w-full items-center justify-between bg-white py-3 text-sm text-gray-400 hover:text-gray-500">
                              <FormLabel
                                sx={{ color: "black" }}
                                className="text-gray-900"
                                id="demo-radio-buttons-group-label"
                              >
                                {section.name}
                              </FormLabel>
                              <span className="ml-6 flex items-center">
                                <PlusIcon
                                  aria-hidden="true"
                                  className="size-5 group-data-[open]:hidden"
                                />
                                <MinusIcon
                                  aria-hidden="true"
                                  className="size-5 group-[&:not([data-open])]:hidden"
                                />
                              </span>
                            </DisclosureButton>
                          </h3>
                          <DisclosurePanel className="pt-6">
                            <div className="space-y-4">
                              <FormControl>
                                <RadioGroup
                                  aria-labelledby="demo-radio-buttons-group-label"
                                  defaultValue="female"
                                  name="radio-buttons-group"
                                >
                                  {section.options.map((option, optionIdx) => (
                                    <>
                                      <FormControlLabel
                                        onChange={(e) =>
                                          handleRadioFilterChange(e, section.id)
                                        }
                                        value={option.value}
                                        control={<Radio />}
                                        label={option.label}
                                      />
                                    </>
                                  ))}
                                </RadioGroup>
                              </FormControl>
                            </div>
                          </DisclosurePanel>
                        </>
                      </Disclosure>
                    ))}
                  </form>
                </div>
              )}

              {/* Product grid */}
              <div className={`${isSearchMode || isAllProductsMode ? 'lg:col-span-5' : 'lg:col-span-4'} w-full`}>
                <div className="flex flex-wrap justify-center bg-white py-5">
                  {products.products &&
                    products.products?.content?.map((item) => (
                      <ProductCard key={item.id} product={item} />
                    ))}
                </div>
              </div>
            </div>
          </section>

          <section className="w-full px=[3.6rem]">
            <div className="px-4 py-5 flex justify-center">
              <Pagination
                count={products.products?.totalPages}
                color="secondary"
                onChange={(e, value) => handlePaginationChange(value)}
              />
            </div>
          </section>
        </main>
      </div>
    </div>
  );
}
