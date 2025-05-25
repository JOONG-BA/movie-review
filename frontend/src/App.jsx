import Header from "./components/layout/Header"
// import Footer from "./components/layout/Footer"

const App = ({ children }) => {
    return (
        <div className="min-h-screen flex flex-col">
            <Header />
            <main className="flex-1 p-4">{children}</main>
            {/*<Footer />*/}
        </div>
    )
}

export default App
