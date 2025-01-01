import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import Header from "../../components/header/Header";
import ProductListing from "../../components/product listing/ProductListing";
import "./CategoryPage.css";

function CategoryPage() {
  const [results, setResults] = useState([]);
  const location = useLocation();
  const [category] = useState(
    new URLSearchParams(location.search).get("category")
  );

  useEffect(() => {
    fetch(`http://localhost:8080/categories/Category/${category}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setResults(data);
      })
      .catch((error) => {
        console.error(error);
        throw error;
      });
  }, [category]);

  return (
    <div className="category-page">
      <Header isAdmin={false} />
      <h1>Products for `{category}` Category</h1>
      {results.length > 0 && (
        <ProductListing products={results} showFilter={false} />
      )}
    </div>
  );
}

export default CategoryPage;
