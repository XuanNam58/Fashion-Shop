"use client";

import { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { MagnifyingGlassIcon, XMarkIcon } from "@heroicons/react/24/outline";
import { fetchSearchSuggestions, clearSearchSuggestions } from "../../../State/Product/Action";

export default function ExpandableProductSearch() {
  const [query, setQuery] = useState("");
  const [showDropdown, setShowDropdown] = useState(false);
  const [isExpanded, setIsExpanded] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState(-1);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const inputRef = useRef();
  const containerRef = useRef();
  const suggestionsRef = useRef();
  const timeoutId = useRef();

  // Get search suggestions from Redux state
  const { searchSuggestions } = useSelector((store) => store.products);
  const { data: suggestions, loading } = searchSuggestions;

  // Handle click outside to collapse search
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        containerRef.current &&
        !containerRef.current.contains(event.target)
      ) {
        setIsExpanded(false);
        setShowDropdown(false);
        setSelectedIndex(-1);
        if (!query) {
          inputRef.current?.blur();
        }
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [query]);

  // Handle escape key
  useEffect(() => {
    const handleEscape = (event) => {
      if (event.key === "Escape") {
        setIsExpanded(false);
        setShowDropdown(false);
        setSelectedIndex(-1);
        inputRef.current?.blur();
      }
    };

    document.addEventListener("keydown", handleEscape);
    return () => {
      document.removeEventListener("keydown", handleEscape);
    };
  }, []);

  // Handle keyboard navigation
  useEffect(() => {
    const handleKeyDown = (event) => {
      if (!showDropdown || suggestions.length === 0) return;

      switch (event.key) {
        case "ArrowDown":
          event.preventDefault();
          setSelectedIndex(prev => 
            prev < suggestions.length - 1 ? prev + 1 : 0
          );
          break;
        case "ArrowUp":
          event.preventDefault();
          setSelectedIndex(prev => 
            prev > 0 ? prev - 1 : suggestions.length - 1
          );
          break;
        case "Enter":
          event.preventDefault();
          if (selectedIndex >= 0 && selectedIndex < suggestions.length) {
            handleSuggestionClick(suggestions[selectedIndex]);
          } else {
            handleSearch(event);
          }
          break;
        case "Tab":
          if (selectedIndex >= 0 && selectedIndex < suggestions.length) {
            event.preventDefault();
            handleSuggestionClick(suggestions[selectedIndex]);
          }
          break;
      }
    };

    document.addEventListener("keydown", handleKeyDown);
    return () => {
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, [showDropdown, suggestions, selectedIndex]);

  // Scroll to selected item
  useEffect(() => {
    if (selectedIndex >= 0 && suggestionsRef.current) {
      const selectedElement = suggestionsRef.current.children[selectedIndex];
      if (selectedElement) {
        selectedElement.scrollIntoView({
          block: "nearest",
          behavior: "smooth"
        });
      }
    }
  }, [selectedIndex]);

  const handleInputChange = (e) => {
    const value = e.target.value;
    setQuery(value);
    setShowDropdown(!!value);
    setSelectedIndex(-1);

    clearTimeout(timeoutId.current);
    timeoutId.current = setTimeout(() => {
      if (value.trim()) {
        dispatch(fetchSearchSuggestions(value, 10)); // Tăng limit lên 10 để có nhiều kết quả hơn
      } else {
        dispatch(clearSearchSuggestions());
      }
    }, 300);
  };

  const handleSearch = (e) => {
    e.preventDefault();
    if (query.trim()) {
      setShowDropdown(false);
      setIsExpanded(false);
      setSelectedIndex(-1);
      // Navigate to search route with query parameter
      navigate(`/search?q=${encodeURIComponent(query.trim())}`);
    }
  };

  const handleSuggestionClick = (product) => {
    setShowDropdown(false);
    setIsExpanded(false);
    setQuery("");
    setSelectedIndex(-1);
    dispatch(clearSearchSuggestions());
    navigate(`/product/${product.id}`);
  };

  const handleFocus = () => {
    setIsExpanded(true);
    setShowDropdown(!!query);
  };

  const handleClear = () => {
    setQuery("");
    setSelectedIndex(-1);
    setShowDropdown(false);
    dispatch(clearSearchSuggestions());
    inputRef.current?.focus();
  };

  const handleBackdropClick = () => {
    setIsExpanded(false);
    setShowDropdown(false);
    setSelectedIndex(-1);
    inputRef.current?.blur();
  };

  return (
    <>
      {/* Backdrop overlay when expanded */}
      {isExpanded && (
        <div
          className="fixed inset-0 bg-black/30 backdrop-blur-sm z-40"
          onClick={handleBackdropClick}
        />
      )}

      <div
        ref={containerRef}
        className={`transition-all duration-300 ease-in-out ${
          isExpanded
            ? "fixed left-1/2 top-20 transform -translate-x-1/2 w-full max-w-3xl px-4 z-50"
            : "relative w-full"
        }`}
      >
        <form onSubmit={handleSearch} className="flex">
          <div className="relative flex-1">
            <input
              ref={inputRef}
              type="text"
              className={`block w-full bg-white border-2 px-4 py-3 text-base placeholder-gray-500 shadow-lg transition-all duration-300 focus:outline-none ${
                isExpanded
                  ? "rounded-l-2xl border-white focus:border-white focus:ring-4 focus:ring-indigo-200/50 text-lg"
                  : "rounded-l-lg border-gray-300 focus:border-indigo-500 focus:ring-2 focus:ring-indigo-200 text-sm"
              }`}
              placeholder={"Search..."}
              value={query}
              onChange={handleInputChange}
              onFocus={handleFocus}
            />

            {/* Clear button */}
            {query && isExpanded && (
              <button
                type="button"
                onClick={handleClear}
                className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600 transition-colors p-1 rounded-full hover:bg-gray-100"
              >
                <XMarkIcon className="h-6 w-6" />
              </button>
            )}
          </div>

          <button
            type="submit"
            className={`inline-flex items-center px-4 py-3 text-white shadow-lg transition-all duration-300 focus:outline-none focus:ring-4 focus:ring-offset-2 ${
              isExpanded
                ? "bg-indigo-600 hover:bg-indigo-700 focus:ring-indigo-300 rounded-r-2xl px-6"
                : "bg-indigo-600 hover:bg-indigo-700 focus:ring-indigo-500 rounded-r-lg"
            }`}
          >
            <MagnifyingGlassIcon
              className={`${isExpanded ? "h-6 w-6" : "h-5 w-5"}`}
              aria-hidden="true"
            />
          </button>
        </form>

        {/* Suggestions dropdown with scroll */}
        {showDropdown && isExpanded && suggestions.length > 0 && (
          <div className="absolute z-50 mt-3 w-full rounded-2xl bg-white shadow-2xl border border-gray-200 max-h-96 overflow-hidden">
            <div 
              ref={suggestionsRef}
              className="py-3 max-h-96 overflow-y-auto scrollbar-thin scrollbar-thumb-gray-300 scrollbar-track-gray-100"
            >
              {suggestions.map((product, index) => (
                <div
                  key={product.id}
                  className={`cursor-pointer px-6 py-4 transition-colors duration-150 border-b border-gray-100 last:border-b-0 ${
                    index === selectedIndex 
                      ? "bg-indigo-50 border-indigo-200" 
                      : "hover:bg-gray-50"
                  }`}
                  onMouseDown={() => handleSuggestionClick(product)}
                  onMouseEnter={() => setSelectedIndex(index)}
                >
                  <div className="flex items-center space-x-4">
                    <div className="flex-shrink-0">
                      <img
                        src={
                          product.imageUrl ||
                          "https://via.placeholder.com/60"
                        }
                        alt={product.title}
                        className="w-14 h-14 object-cover rounded-xl shadow-sm"
                      />
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-lg font-medium text-gray-900 truncate">
                        {product.title}
                      </p>
                      {product.price && (
                        <p className="text-xl font-semibold text-indigo-600 mt-1">
                          {typeof product.price === "number"
                            ? `${product.price.toLocaleString("vi-VN")}đ`
                            : product.price}
                        </p>
                      )}
                    </div>
                    <div className="flex-shrink-0">
                      <MagnifyingGlassIcon className="h-6 w-6 text-gray-400" />
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Loading state */}
        {showDropdown && isExpanded && loading && (
          <div className="absolute z-50 mt-3 w-full rounded-2xl bg-white shadow-2xl border border-gray-200">
            <div className="px-6 py-12 text-center">
              <div className="inline-flex items-center space-x-3">
                <div className="animate-spin rounded-full h-6 w-6 border-b-2 border-indigo-600"></div>
                <span className="text-lg text-gray-600">Đang tìm kiếm...</span>
              </div>
            </div>
          </div>
        )}

        {/* Empty state */}
        {showDropdown &&
          isExpanded &&
          !loading &&
          suggestions.length === 0 &&
          query && (
            <div className="absolute z-50 mt-3 w-full rounded-2xl bg-white shadow-2xl border border-gray-200">
              <div className="px-6 py-12 text-center">
                <p className="text-lg text-gray-500">No products found </p>
                <p className="text-sm text-gray-400 mt-2">
                  Try searching with different keywords
                </p>
              </div>
            </div>
          )}
      </div>
    </>
  );
}
