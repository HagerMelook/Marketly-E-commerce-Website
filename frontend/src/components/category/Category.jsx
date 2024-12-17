import PropTypes from "prop-types";
// import ProductListing from "./ProductListing";
// import { useState } from "react";
import "./Category.css";

function Category({ category }) {
  // const [products, setProducts] = useState([]);

  function requestCategotyProducts(categoryName) {
    return fetch("http://localhost:8080/Category/" + categoryName, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data) => {
        // setProducts(data);
        return data;
      })
      .catch((error) => {
        console.error(error);
        throw error;
      });
  }
  // async function removeBackground(imageUrl) {
  //   const response = await fetch(`https://api.remove.bg/v1.0/removebg`, {
  //     method: "POST",
  //     headers: {
  //       "X-Api-Key": 39nH2XMTcoAxnogxmqVBuAXH,
  //       "Content-Type": "application/octet-stream",
  //     },
  //     body: imageUrl,
  //   });

  //   if (response.ok) {
  //     const blob = await response.blob();
  //     const imageUrl = URL.createObjectURL(blob);
  //     return imageUrl;
  //   // } else {
  //     throw new Error("Error removing background");
  //   }
  // }

  return (
    <div
      className="category"
      onClick={
        () => requestCategotyProducts(category.categoryName)
        // (<ProductListing key={1} products={products} showFilter={false} />))
      }
    >
      <img src={category.categoryImagePath} alt={category.name} />
      <h2>{category.categoryName}</h2>
    </div>
  );
}

Category.propTypes = {
  category: PropTypes.object,
};

export default Category;
