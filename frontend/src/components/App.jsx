import { BrowserRouter, Route, Routes } from "react-router-dom";
import ClientReg from '../pages/ClientReg/ClientReg';
import RadialChoice from '../pages/RadialChoice/RadialChoice';
import VendorReg from '../pages/VendorReg/VendorReg';

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<RadialChoice />} />
                <Route path="/clientSignUp" element={<ClientReg />} />
                <Route path="/vendorSignUp" element={<VendorReg />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App