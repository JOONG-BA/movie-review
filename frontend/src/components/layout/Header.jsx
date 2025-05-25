import { Link } from "react-router-dom"

const Header = () => (
    <header className="bg-primary text-white p-4 flex justify-between">
        <Link to="/" className="font-bold">🎬 MovieLog</Link>
        <nav className="flex gap-4">
            <Link to="/login">로그인</Link>
            <Link to="/signup">회원가입</Link>
            <Link to="/mypage">마이페이지</Link>
        </nav>
    </header>
)

export default Header
