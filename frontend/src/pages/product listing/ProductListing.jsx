import PropTypes from "prop-types";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Filter from "../../components/filter/Filter";
import "./ProductListing.css";

function ProductListing({ products = [], showFilter = false }) {
    const navigate = useNavigate();
    const [filteredProducts, setFilteredProducts] = useState(products);
    const [selectedCategories, setSelectedCategories] = useState([]);

    useEffect(() => {
        setFilteredProducts(products);
    }, [products]);

    const categories = [...new Set(products.map((product) => product.category))];

    const minPrice = Math.floor(Math.min(...products.map((product) => product.price)));
    const maxPrice = Math.ceil(Math.max(...products.map((product) => product.price)));

    const minRating = Math.floor(Math.min(...products.map((product) => product.rating)));
    const maxRating = Math.ceil(Math.max(...products.map((product) => product.rating)));


    const handleFilterChange = (price_range, rating_range, selected_categories) => {
        const [minPrice, maxPrice] = price_range;
        const [minRating, maxRating] = rating_range;
        setSelectedCategories(selected_categories);
        const updatedProducts = products.filter(
            (product) => {
                const matchesCategory = selected_categories.length === 0 || selected_categories.includes(product.category);
                const matchedRating = product.rating >= minRating && product.rating <= maxRating;
                const matchedPrice = product.price >= minPrice && product.price <= maxPrice;

                return matchedPrice && matchedRating && matchesCategory;
            }
        );
        setFilteredProducts(updatedProducts);
    };

    const renderStars = (rating) => {
        const fullStars = Math.min(Math.floor(rating), 5);
        const halfStar = rating % 1 >= 0.5;
        const emptyStars = 5 - fullStars - (halfStar ? 1 : 0);

        return (
            <>
                {Array(fullStars)
                    .fill()
                    .map((_, i) => (
                        <span key={`full-${i}`} className="star">
                            ★
                        </span>
                    ))}
                {halfStar && <span className="star">☆</span>}
                {Array(emptyStars)
                    .fill()
                    .map((_, i) => (
                        <span key={`empty-${i}`} className="star empty">
                            ★
                        </span>
                    ))}
            </>
        );
    };

    return (
        <div className="container">
            <aside className="sidebar">
                <Filter
                    categories={categories}
                    minRating={minRating}
                    maxRating={maxRating}
                    minPrice={minPrice}
                    maxPrice={maxPrice}
                    selectedCategories={selectedCategories}
                    onFilterChange={handleFilterChange}
                    showFilter={showFilter}
                />
            </aside>
            <main className="main">
                <div className="product-list">
                    {filteredProducts.map((product) => (
                        <div className="product-card" key={product.id} onClick={() => navigate(`productPage/${product.id}`)}>
                            <img src={product.image} alt={product.name} className="product-image" />
                            <h3 className="product-name">{product.name}</h3>
                            <div className="product-rating">{renderStars(product.rating)}</div>
                            <p className="product-price">{product.price} EGP</p>
                        </div>
                    ))}
                </div>
            </main>
        </div>
    );
};

ProductListing.propTypes = {
    products: PropTypes.arrayOf(
        PropTypes.shape({
            id: PropTypes.number.isRequired,
            image: PropTypes.string.isRequired,
            name: PropTypes.string.isRequired,
            category: PropTypes.string.isRequired,
            rating: PropTypes.number.isRequired,
            price: PropTypes.number.isRequired,
        })
    ),
    showFilter: PropTypes.bool,
};

export default ProductListing;