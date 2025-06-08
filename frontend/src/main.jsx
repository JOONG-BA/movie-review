import React from "react"
import ReactDOM from "react-dom/client"
import {BrowserRouter, useLocation, useRoutes} from "react-router-dom"
import routes from "~react-pages"
import App from "./App" // 여기에서 공통 레이아웃, Provider 묶음
import "./App.css";
import NotFound from "@/pages/404.jsx";

const Router = () => {
    const element = useRoutes([
        ...routes,
        {
            path: "*",
            element: <NotFound />
        }
    ]);

    const location = useLocation();
    const is404 = location.pathname === "/404" || location.pathname === "*" || !routes.some(route => location.pathname.startsWith(`/${route.path}`));

    return is404 ? element : <App>{element}</App>;
}

ReactDOM.createRoot(document.getElementById("root")).render(
    <React.StrictMode>
        <BrowserRouter>
            <Router />
        </BrowserRouter>
    </React.StrictMode>
)
