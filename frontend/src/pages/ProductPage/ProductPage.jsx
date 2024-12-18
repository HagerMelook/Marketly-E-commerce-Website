import { useParams } from 'react-router-dom';
import './ProductPage.css';
import Header from '../../components/header/Header';
import { useEffect, useState } from 'react';
import ProductInfoContainer from '../../components/product page/ProductInfoContainer/ProductInfoContainer';
import * as ProductPageApi from '../../components/API/ProductPageApi'
import Description from '../../components/product page/Description/Description'
import Comments from '../../components/product page/Comments/Comments'

function ProductPage() {
  const { productId } = useParams();

  const [ images, setImages ] = useState();
  const [ product, setProduct ] = useState();

  useEffect(() => {
    ProductPageApi.getImages(productId).then((data) => setImages(data));
    ProductPageApi.getProductInfo(productId).then((data) => setProduct(data));
  }, [productId]);

  return (
    <div className='product-page-container'>
        <Header />

        {product && (
          <>
          <ProductInfoContainer images={images} product={product}/>
        
          <Description product={product}/>

          <Comments productRating={product.rating} productId={product.id} accountId={2}/>
          </>
        )}
    </div>
  );
}

export default ProductPage;
