import PropTypes from "prop-types";
import { useState } from "react";
import ReactSlider from "react-slider";
import "./Filter.css";

function Filter({ categories, minRating, maxRating, minPrice, maxPrice, selectedCategories, onFilterChange, showFilter }){
    
    const [ratingRange, setRatingRange] = useState([minRating, maxRating]);
    const [priceRange, setPriceRange] = useState([minPrice, maxPrice]);

    const handlePriceChange = (newPriceRange) => {
        setPriceRange(newPriceRange);
        onFilterChange(newPriceRange, ratingRange, selectedCategories);
    };

    const handleRatingChange = (newRatingRange) => {
        setRatingRange(newRatingRange);
        onFilterChange(priceRange, newRatingRange, selectedCategories);
    };

    const handleCategoryToggle = (category) => {
        const updatedCategories = selectedCategories.includes(category)
            ? selectedCategories.filter((cat) => cat !== category)
            : [...selectedCategories, category];
        selectedCategories = updatedCategories;
        onFilterChange(priceRange, ratingRange, updatedCategories);
    };

    return (
        <div>
            <div className="filter">
                <h3>Filter by Price</h3>
                <p>
                    Price Range: <strong>EGP {priceRange[0]}</strong> - <strong>EGP {priceRange[1]}</strong>
                </p>
                <ReactSlider
                    className="slider"
                    thumbClassName="thumb"
                    trackClassName="track"
                    value={priceRange}
                    min={minPrice}
                    max={maxPrice}
                    step={1}
                    onChange={handlePriceChange}
                    ariaLabel={"Price Range"}
                    pearling
                    renderThumb={(props) => (
                        <div {...props}>
                        </div>
                    )}
                />
            </div>
            <div className="seperator"></div>
            <div className="filter">
                <h3>Filter by Rating</h3>
                <p>
                    Rating Range: <strong>{ratingRange[0]}</strong> - <strong>{ratingRange[1]}</strong>
                </p>
                <ReactSlider
                    className="slider"
                    thumbClassName="thumb"
                    trackClassName="track"
                    value={ratingRange}
                    min={minRating}
                    max={maxRating}
                    step={1}
                    onChange={handleRatingChange}
                    ariaLabel={"Rating Range"}
                    pearling
                    renderThumb={(props) => (
                        <div {...props}>
                        </div>
                    )}
                />
            </div>
            <div className="seperator"></div>
            {showFilter && (
                <div>
                    <h3 className="sidebar-category-title">Filter by Categories</h3>
                    {categories.map((category) => (
                        <div className="category-item" key={category}>
                            <input
                                type="checkbox"
                                id={category}
                                checked={selectedCategories.includes(category)}
                                onChange={() => handleCategoryToggle(category)}
                            />
                            <label htmlFor={category}>{category}</label>
                        </div>
                    ))}
                </div>
            )}

        </div>
    );
};

Filter.propTypes = {
    categories: PropTypes.array.isRequired,
    minPrice: PropTypes.number.isRequired,
    maxPrice: PropTypes.number.isRequired,
    minRating: PropTypes.number.isRequired,
    maxRating: PropTypes.number.isRequired,
    selectedCategories: PropTypes.array.isRequired,
    onFilterChange: PropTypes.func.isRequired,
    showFilter: PropTypes.bool.isRequired,
};

export default Filter;