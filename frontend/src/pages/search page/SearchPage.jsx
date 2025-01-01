// import PropTypes from "prop-types";
import { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import Header from "../../components/header/Header";
import ProductListing from "../../components/product listing/ProductListing";
import "./SearchPage.css";

function SearchPage() {
  const [results, setResults] = useState([]);
  const location = useLocation();
  const [word, setWord] = useState(
    new URLSearchParams(location.search).get("word")
  );

  useEffect(() => {
    setWord(new URLSearchParams(location.search).get("word"));
    if (word) {
      console.log("Fetching search results for", word);
      fetchSearchResults(word);
    }
  }, [location, word]);

  async function fetchSearchResults(searchWord) {
    return await fetch(`http://localhost:8080/Search/${searchWord}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${sessionStorage.getItem('token')}`
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setResults(data);
        return data;
      })
      .catch((error) => {
        console.error(error);
        throw error;
      });
  }

  return (
    <div className="search-page">
      <Header isAdmin={false} />
      <h1>Search Results for `{word}`</h1>
      {console.log("numebr of results", results.length)}
      {console.log("results", results)}
      {results.length > 0 && (
        <ProductListing products={results} showFilter={true} />
      )}
    </div>
  );
}

export default SearchPage;
