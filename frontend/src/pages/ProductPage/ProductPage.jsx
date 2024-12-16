import { useParams } from 'react-router-dom';

function ProductPage() {
  const { productId } = useParams();

  return <h1>Page Of Product With ID: {productId}</h1>;
}

export default ProductPage;
