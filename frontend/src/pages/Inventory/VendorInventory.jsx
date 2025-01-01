import { useState, useEffect, useMemo} from "react";
import { useNavigate } from "react-router-dom";
import { useProductContext} from "../../contexts/ProductProvider"
import Header from "../../components/header/Header";
import searchIcon from "../../assets/search-icon.svg";
import InventoryProductListing from "../../components/Inventory/InventoryProductListing";
import Footer from "../../components/footer/Footer";
import './VendorInventory.css';

function VendorInventory() {
    const [products, setProducts] = useState([]);
    const [allProducts, setAllProducts] = useState([]);
    const [categories, setCategories] = useState([]);
    const [isPending, setIsPending] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const {setSelectedProduct} = useProductContext();
    const navigate = useNavigate();
    const productsPerPage = 4;
    const id = 10;

    useEffect(() => {
        fetch(`http://localhost:8080/vendor/products/${id}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${sessionStorage.getItem('token')}`,
            }
        })
            .then(res => {
                if (!res.ok) {
                    throw Error('Could not fetch products');
                }
                return res.json();
            })
            .then(data => {
                setProducts(data);
                setAllProducts(data);
                setIsPending(false);
                setError(null);
            })
            .catch(error => setError(error.message));

        fetch('http://localhost:8080/vendor/categories',{
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${sessionStorage.getItem('token')}`,
            }
        })
            .then(res => res.json())
            .then(data => setCategories(data))
            .catch(error => console.error(error)); 
    }, []);

    
    const currentProducts = useMemo(() => {
        const start = (currentPage - 1) * productsPerPage;
        const end = start + productsPerPage;
        return products.slice(start, end);
    }, [products, currentPage]);

    
    const numbers = useMemo(() => {
        return [...Array(Math.ceil(products.length / productsPerPage)).keys()].map(n => n + 1);
    }, [products]);

    const selectCategory = (event) => {
        const selected = event.target.value;
        if (selected) {
            setProducts(allProducts.filter(product => product.category === selected));
        } else {
            setProducts(allProducts);
        }
        setCurrentPage(1);
    };

    const handleSearch = (event) => {
        event.preventDefault();
        const searched = event.target.elements.search.value;
        if(searched){
            fetch(`http://localhost:8080/vendor/search/${id}/${searched}`,{
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${sessionStorage.getItem('token')}`,
                }
            })
            .then(res => {
                if (!res.ok) {
                    throw Error('Could not fetch products');
                }
                return res.json();
            })
            .then(data => {
                setProducts(data);
                setIsPending(false);
                setError(null);
                setCurrentPage(1);
            })
            .catch(error => setError(error.message));
        }
        else{
            setProducts(allProducts);
            setIsPending(false);
            setError(null);
            setCurrentPage(1);
        }
    };


    const prePage = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
        }
    };

    const nextPage = () => {
        if (currentPage < numbers.length) {
            setCurrentPage(currentPage + 1);
        }
    };

    const changePage = (id) => setCurrentPage(id);

    const handleRemoveProduct = (id) => {
        setProducts(products.filter(product => product.id !== id));
    };


    const handleCreate = () => {
        setSelectedProduct(null)
        navigate('product-form')
    };

    return (
        <>
        <div className="vendor-inventory">
            <Header isVendor={true} />
            <div className="inventory-bar">
                <form className="inventory-search-bar" onSubmit={handleSearch}>
                    <input type="text" name="search" placeholder="Search for products..." />
                    <button type="submit">
                        <img src={searchIcon} className="icons" alt="search" />
                    </button>
                </form>
                <select className="filter-dropdown" name="filter" onChange={selectCategory} style={{color: 'black'}}>
                    <option value="">All Products</option>
                    {categories.map(category => (
                        <option key={category} value={category}>
                            {category}
                        </option>
                    ))}
                </select>
                <button className="create-button" onClick={handleCreate}>
                    Create
                </button>
            </div>

            {error && <div style={{ width: '100%', textAlign: 'center' }}>{error}</div>}
            {isPending && !error && <div style={{ width: '100%', textAlign: 'center' }}>Loading...</div>}
            <InventoryProductListing
                    products={currentProducts}
                    onRemove={handleRemoveProduct}
            />

            {numbers.length > 0 && (
                <nav className="page-nav">
                    <ul className="pagination">
                        <li className="page-item">
                            <button className="page-link" onClick={prePage}>Prev</button>
                        </li>
                        {numbers.map(n => (
                            <li key={n} className={`page-item ${currentPage === n ? 'active' : ''}`}>
                                <button className="page-link" onClick={() => changePage(n)}>
                                    {n}
                                </button>
                            </li>
                        ))}
                        <li className="page-item">
                            <button className="page-link" onClick={nextPage}>Next</button>
                        </li>
                    </ul>
                </nav>
            )}
        </div>
        <Footer />
        </>
    );
}

export default VendorInventory;



