import { BrowserRouter, Route, Routes } from "react-router-dom";
import ClientReg from './ClientReg/ClientReg';
import RadialChoice from './RadialChoice/RadialChoice';
import VendorReg from './VendorReg/VendorReg';

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