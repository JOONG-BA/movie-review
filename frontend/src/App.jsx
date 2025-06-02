import Header from "./components/layout/Header"
import Footer from "./components/layout/Footer"
import { AuthProvider } from "@/context/AuthContext"

const App = ({ children }) => {
  return (
    <AuthProvider>
      <div className="min-h-screen flex flex-col">
        <Header />
        <main className="flex-1 flex flex-col">{children}</main>
        <Footer />
      </div>
    </AuthProvider>
  )
}

export default App