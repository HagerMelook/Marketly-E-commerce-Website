
import Header from "../../components/header/Header.jsx";
import Category from "../../components/category/Category.jsx";
import PropTypes from "prop-types";
import { useState, useEffect } from "react";
import "./Home.css";

function Home({ isAdmin, isVendor }) {
  const [categories, setCategories] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/Home", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        setCategories(data);
      })
      .catch((error) => {
        console.error(error);
        throw error;
      });
  });

  return (
    <div className="home">
      <Header isAdmin={isAdmin} isVendor={isVendor} />
      <div className="categories">
        {categories.map((category) => (
          <Category key={category.name} category={category} />
        ))}
      </div>

    </div>
  );
}

Home.propTypes = {
  isAdmin: PropTypes.bool.isRequired,

  isVendor: PropTypes.bool.isRequired,
};
Category.propTypes = {
  category: PropTypes.object,

};

export default Home;
