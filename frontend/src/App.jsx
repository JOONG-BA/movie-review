import Header from "./components/layout/Header"

const App = ({ children }) => {
    return (
        <div className="min-h-screen flex flex-col">
            <Header />
            <main className="flex-1">{children}</main>
            {/*<Footer />*/}
        </div>
    )
}

export default App
