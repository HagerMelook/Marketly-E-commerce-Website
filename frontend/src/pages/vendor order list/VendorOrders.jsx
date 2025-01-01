import { useState, useEffect } from 'react';
import Header from '../../components/header/Header';
import './VendorOrders.css';
import {fetchVendorOrders, fetchClientInfo} from '../../components/API/VendorOrdersApi';

const VendorOrders = () => {
  const [orders, setOrders] = useState([]);
  const [page, setPage] = useState(0);
  const [sortDir, setSortDir] = useState('dec');
  const [status, setStatus] = useState('');
  const [visibleClientInfo, setVisibleClientInfo] = useState({});
  const [isFirst, SetIsFirst] = useState(null);
  const [isLast, SetIsLast] = useState(null);
  const userRole = sessionStorage.getItem('role');
  const pageSize = 5;

  useEffect(() => {
    fetchOrders();
  }, [page, sortDir, status]);

  const fetchOrders = async () => {
    try {
      const data = await fetchVendorOrders(page, pageSize, sortDir, status);
      setOrders(data.content);
      SetIsFirst(data.first);
      SetIsLast(data.last);
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  };

  const toggleClientInfo = async (accountId, orderKey) => {
    setVisibleClientInfo((prev) => ({
      ...prev,
      [orderKey]: !prev[orderKey]
    }));

    if (!visibleClientInfo[orderKey]) {
      try {
        const data = await fetchClientInfo(accountId);
        setVisibleClientInfo((prev) => ({
          ...prev,
          [orderKey]: data
        }));
      } catch (error) {
        console.error('Error fetching client info:', error);
      }
    }
  };

  return (
    <>
        <Header isVendor={userRole==='[vendor]'}  isAdmin={userRole==="[admin]"}/>

        <div className="vendor-orders-wrapper">
            <h1 style={{marginBottom: "auto"}}>Orders History</h1>
            <div className="vendor-orders-filters">
                <div className="vendor-orders-sort">
                <label className="vendor-orders-label" htmlFor="sort">Sort By Date:</label>
                <select className="vendor-orders-select" id="sort" value={sortDir} onChange={(e) => setSortDir(e.target.value)}>
                    <option value="asc">Older</option>
                    <option value="dec">More Recent</option>
                </select>
                </div>
                <div className="vendor-orders-status">
                <label className="vendor-orders-label" htmlFor="status">Filter By Status:</label>
                <select className="vendor-orders-select" id="status" value={status} onChange={(e) => {setPage(0); setStatus(e.target.value);}}>
                    <option value="">All</option>
                    <option value="confirmed">Confirmed</option>
                    <option value="processing">Processing</option>
                    <option value="packaged">Packaged</option>
                    <option value="shipped">Shipped</option>
                    <option value="delivered">Delivered</option>
                </select>
                </div>
            </div>
            {orders.map((order) => {
                const orderKey = `${order.accountId}-${order.productName}-${order.date}`;
                const clientData = visibleClientInfo[orderKey];
                return (
                <div className="vendor-orders-card" key={orderKey}>
                    <h3 className="vendor-orders-product">{order.productName}</h3>
                    <p className="vendor-orders-info">{order.username} - {new Date(order.date).toLocaleDateString()}</p>
                    <p className="vendor-orders-status-text">Status: {order.status}</p>
                    <button
                    className="vendor-orders-btn"
                    onClick={() => toggleClientInfo(order.accountId, orderKey)}
                    >
                    {clientData ? 'Hide Client Info' : 'Show Client Info'}
                    </button>
                    {clientData && clientData !== true && (
                    <div className="vendor-orders-client-info">
                        <p>{clientData.firstName} {clientData.lastName}</p>
                        <p>{clientData.address}</p>
                        <p>{clientData.phone}</p>
                        <p>{clientData.postalCode}</p>
                    </div>
                    )}
                </div>
                );
            })}
            <div className="vendor-orders-pagination">
                <button className="vendor-orders-btn" onClick={() => setPage(page - 1)} disabled={isFirst}>Previous</button>
                <button className="vendor-orders-btn" onClick={() => setPage(page + 1)} disabled={isLast}>Next</button>
            </div>
        </div>
    </>
  );
};

export default VendorOrders;
