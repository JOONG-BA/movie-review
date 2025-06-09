import React from "react"
import ReactDOM from "react-dom/client"
import {BrowserRouter, useRoutes} from "react-router-dom"
import routes from "~react-pages"
import NotFound from "@/components/common/404.jsx";
import { match } from "path-to-regexp";
import App from "./App" // 여기에서 공통 레이아웃, Provider 묶음
import "./App.css";

const extractPaths = (routes, basePath = "") => {
    const paths = [];

    for (const route of routes) {
        const fullPath = basePath + (route.path ? `/${route.path}` : "");
        if (route.element) {
            paths.push(fullPath.replace(/\/+/g, "/")); // 중복 슬래시 제거
        }
        if (route.children) {
            paths.push(...extractPaths(route.children, fullPath));
        }
    }

    return paths;
};

const Router = () => {
    const element = useRoutes([
        ...routes,
        {
            path: "*",
            element: <NotFound />
        }
    ]);

    const allPaths = extractPaths(routes);
    const is404 = !allPaths.some((path) => {
        const matcher = match(path, { decode: decodeURIComponent });
        return matcher(location.pathname);
    });

    return is404 ? element : <App>{element}</App>;
}

ReactDOM.createRoot(document.getElementById("root")).render(
    <React.StrictMode>
        <BrowserRouter>
            <Router />
        </BrowserRouter>
    </React.StrictMode>
)
