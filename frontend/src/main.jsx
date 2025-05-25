import React from "react"
import ReactDOM from "react-dom/client"
import { BrowserRouter, useRoutes } from "react-router-dom"
import routes from "~react-pages"
import App from "./App" // 여기에서 공통 레이아웃, Provider 묶음

const Router = () => {
    const element = useRoutes(routes)
    return <App>{element}</App>
}

ReactDOM.createRoot(document.getElementById("root")).render(
    <React.StrictMode>
        <BrowserRouter>
            <Router />
        </BrowserRouter>
    </React.StrictMode>
)
