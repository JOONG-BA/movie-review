import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button.jsx"
import { Input } from "@/components/ui/input.jsx"
import { IoMdSearch } from "react-icons/io"
import { LoginDialog } from "@/components/ui/LoginDialog.jsx"
import { SignupDialog } from "@/components/ui/SignupDialog.jsx"

const Header = () => (
  <header className="bg-primary text-white flex items-center justify-center">
    <div className="w-full flex justify-between items-center py-3 container">
      <Link to="/" className="text-3xl tracking-tighter">🎬 MOVIELOG</Link>
      <div className="flex gap-5">
        <form className="flex relative">
          <Input className="bg-gray-800 rounded-xs w-70 border-0" placeholder="찾는 영화가 있으신가요?" />
          <Button className="absolute bg-0 right-0 text-2xl p-0 hover:bg-0 text-gray-400 hover:text-white cursor-pointer">
            <IoMdSearch className="size-6" />
          </Button>
        </form>
        <nav className="flex gap-3">
          <LoginDialog />
          <SignupDialog />
        </nav>
      </div>
    </div>
  </header>
)

export default Header
