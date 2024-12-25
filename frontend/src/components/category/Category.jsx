import PropTypes from "prop-types";
// import ProductListing from "./ProductListing";
// import { nav } from "react";
import { useNavigate } from "react-router-dom";
import "./Category.css";

function Category({ category }) {
  const navigate = useNavigate();

  return (
    <div
      className="category"
      onClick={() => {
        console.log("Navigating to /category/" + category.categoryName);
        navigate(`/category?category=${category.categoryName}`);
      }}
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
