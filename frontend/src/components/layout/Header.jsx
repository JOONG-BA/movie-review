import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button.jsx"
import { Input } from "@/components/ui/input.jsx"
import { IoMdSearch } from "react-icons/io"
import { LoginDialog } from "@/components/ui/LoginDialog.jsx"
import { SignupDialog } from "@/components/ui/SignupDialog.jsx"
import { useState } from "react"

const Header = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(true)

  return (
    <header className="fixed w-full z-50 bg-gray-900 text-white flex items-center justify-center overflow-hidden top-0">
      <div className="w-full flex justify-between items-center py-3 container">
        <Link to="/" className="text-xs sm:text-3xl tracking-tighter">MOVIELOG</Link>

        <div className="flex gap-5">
          <form className="flex relative">
            <Input className="bg-gray-800 rounded-xs w-[250px] border-0" placeholder="찾는 영화가 있으신가요?" />
            <Button className="absolute bg-0 right-0 text-2xl p-0 hover:bg-0 text-gray-400 hover:text-white cursor-pointer">
              <IoMdSearch className="size-6" />
            </Button>
          </form>

          <nav className="flex items-center gap-3">
            {isLoggedIn ? (
              <>
                <Link to="/mypage" className="w-9 h-9 rounded-full overflow-hidden border border-white">
                  <img
                    src="https://i.namu.wiki/i/m1WHrelfgKjmdgckinSKZApCLjRnRvMVoJFtsyJ_ahL21yTZMZxChJW0gG01uh2JzljEHYhvmzdhxCqQ_lhPv61XV-GaEVZhJvILmJpHC2s2E2sKbdrF21sznEoFwdbwFoC9CQVosHGQKurnt7Atig.webp"
                    alt="profile"
                    className="object-cover w-full h-full"
                  />
                </Link>

                <Link to="/mypage">
                  <Button className="bg-gray-700 hover:bg-gray-600 text-white px-4 py-2 text-sm rounded-md">
                    마이페이지
                  </Button>
                </Link>
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
