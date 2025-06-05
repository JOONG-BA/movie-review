import { useContext } from "react"
import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button.jsx"
import { Input } from "@/components/ui/input.jsx"
import { IoMdSearch } from "react-icons/io"
import { LoginDialog } from "@/components/ui/LoginDialog.jsx"
import { SignupDialog } from "@/components/ui/SignupDialog.jsx"
import { AuthContext } from "@/context/AuthContext.jsx"
import MovieSearchForm from "@/components/moive/search/MovieSearchForm.jsx";

const Header = () => {
  const { isLoggedIn, logout } = useContext(AuthContext)

  return (
    <header className="fixed w-full z-50 bg-gray-900 text-white flex items-center justify-center overflow-hidden top-0">
      <div className="w-full flex justify-between items-center py-3 container">
        <Link to="/" className="text-xs sm:text-3xl tracking-tighter">MOVIELOG</Link>
        <div className="flex gap-5">
          <MovieSearchForm />
          <nav className="flex items-center gap-3">
            {isLoggedIn ? (
              <>
                <Link to="/mypage">
                  <Button className="bg-gray-700 hover:bg-gray-600 text-white px-4 py-2 text-sm rounded-md">
                    마이페이지
                  </Button>
                </Link>
                <Button
                  className="bg-gray-700 hover:bg-gray-600 text-white px-4 py-2 text-sm rounded-md"
                  onClick={logout}
                >
                  로그아웃
                </Button>
              </>
            ) : (
              <>
                <LoginDialog />
                <SignupDialog />
              </>
            )}
          </nav>
        </div>
      </div>
    </header>
  )
}

export default Header
