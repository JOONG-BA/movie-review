import { Link } from "react-router-dom"
import {Button} from "@/components/ui/button.jsx";
import {Input} from "@/components/ui/input.jsx";
import { IoMdSearch } from "react-icons/io";

const Header = () => (
    <header className="bg-primary text-white flex items-center justify-center">
        <div className="w-full flex justify-between items-center py-3 container ">
            <Link to="/" className="text-3xl tracking-tighter">MOVIELOG</Link>
            <div className="flex gap-5">
                <form className="flex relative">
                    <Input className="bg-gray-800 rounded-xs w-70 border-0" placeholder="찾는 영화가 있으신가요?">
                    </Input>
                    <Button className="absolute bg-0 right-0 text-2xl p-0 hover:bg-0 text-gray-400 hover:text-white cursor-pointer">
                        <IoMdSearch className="size-6"></IoMdSearch>
                    </Button>
                </form>
                <nav className="flex gap-3">
                    <Button className="rounded-sm text-gray-400 hover:text-white delay-3" asChild={true}><Link to="/login">로그인</Link></Button>
                    <Button className="rounded-sm border border-gray-500 hover:bg-gray-800 delay-3" asChild={true}><Link to="/signup">회원가입</Link></Button>
                    {/*<Link to="/mypage">마이페이지</Link>*/}
                </nav>
            </div>
        </div>
    </header>
)

export default Header
