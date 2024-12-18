import { useState, useContext, createContext } from "react";
import PropTypes from "prop-types";

const ProdcutContext = createContext();

function ProductProvider({ children }) {
  const [selectedProduct, setSelectedProduct] = useState(null);

  return (
    <ProdcutContext.Provider value={{ selectedProduct, setSelectedProduct }}>
      {children}
    </ProdcutContext.Provider>
  );
}

ProductProvider.propTypes = {
  children: PropTypes.node.isRequired,
};

function useProductContext() {
  return useContext(ProdcutContext);
}

export { ProductProvider, useProductContext };
