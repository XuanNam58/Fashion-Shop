import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import { api, API_BASE_URL } from "../../../config/apiConfig";


export default function ProductSearch() {
  const [query, setQuery] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [showDropdown, setShowDropdown] = useState(false);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const inputRef = useRef();
  let timeoutId = useRef();

  const fetchSuggestions = async (q) => {
    console.log("q", q)
    if (!q) {
      setSuggestions([]);
      return;
    }
    setLoading(true);
    try {
      const res = await api.get(
        `${API_BASE_URL}/api/products/suggestions?q=${encodeURIComponent(q)}&limit=5`
      );
      if (res) {
        console.log("data search", res)
        setSuggestions(res.data);
      } else {
        setSuggestions([]);
      }
    } catch (e) {
      setSuggestions([]);
    }
    setLoading(false);
  };

  const handleInputChange = (e) => {
    const value = e.target.value;
    setQuery(value);
    setShowDropdown(!!value);
    clearTimeout(timeoutId.current);
    timeoutId.current = setTimeout(() => {
      fetchSuggestions(value);
    }, 300);
  };

  const handleSearch = (e) => {
    e.preventDefault();
    if (query.trim()) {
      setShowDropdown(false);
      navigate(`/search?q=${encodeURIComponent(query.trim())}`);
    }
  };

  const handleSuggestionClick = (product) => {
    setShowDropdown(false);
    setQuery("");
    navigate(`/product/${product.id}`);
  };

  const handleBlur = () => {
    setTimeout(() => setShowDropdown(false), 150);
  };

  return (
    <div className="relative w-full max-w-xs lg:max-w-md">
      <form onSubmit={handleSearch} className="flex">
        <input
          ref={inputRef}
          type="text"
          className="block w-full rounded-l-md border border-gray-300 px-3 py-2 text-sm focus:border-indigo-500 focus:ring-indigo-500"
          placeholder="Search..."
          value={query}
          onChange={handleInputChange}
          onFocus={() => setShowDropdown(!!query)}
          onBlur={handleBlur}
        />
        <button
          type="submit"
          className="inline-flex items-center rounded-r-md bg-indigo-600 px-3 py-2 text-white hover:bg-indigo-700 focus:outline-none"
        >
          <MagnifyingGlassIcon className="h-5 w-5" aria-hidden="true" />
        </button>
      </form>
      {showDropdown && suggestions.length > 0 && (
        <ul className="absolute z-20 mt-1 w-full rounded-md bg-white shadow-lg border border-gray-200 max-h-60 overflow-auto">
          {suggestions.map((product) => (
            <li
              key={product.id}
              className="cursor-pointer px-4 py-2 hover:bg-gray-100 flex items-center"
              onMouseDown={() => handleSuggestionClick(product)}
            >
              <img
                src={product.imageUrl || product.image || "https://via.placeholder.com/40"}
                alt={product.title}
                className="w-8 h-8 object-cover rounded mr-3"
              />
              <span className="truncate">{product.title}</span>
              <span className="truncate">{product.price}</span>
            </li>
          ))}
        </ul>
      )}
      {showDropdown && loading && (
        <div className="absolute z-20 mt-1 w-full rounded-md bg-white shadow-lg border border-gray-200 p-2 text-center text-gray-500 text-sm">
          Loading...
        </div>
      )}
    </div>
  );
} 