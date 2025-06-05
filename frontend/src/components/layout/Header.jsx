import {useContext, useEffect, useRef, useState} from "react";
import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button.jsx";
import { AuthContext } from "@/context/AuthContext.jsx";
import MovieSearchForm from "@/components/moive/search/MovieSearchForm.jsx";
import { LoginDialog } from "@/components/ui/LoginDialog.jsx";
import { SignupDialog } from "@/components/ui/SignupDialog.jsx";
import { FiMenu, FiX } from "react-icons/fi";

const Header = () => {
  const { isLoggedIn, logout } = useContext(AuthContext);
  const [menuOpen, setMenuOpen] = useState(false);
  const menuRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setMenuOpen(false);
      }
    };

    if (menuOpen) {
      document.addEventListener("mousedown", handleClickOutside);
    } else {
      document.removeEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [menuOpen]);

  return (
      <header className="fixed w-full z-50 bg-gray-900 text-white top-0">
        <div className="container mx-auto px-4 py-3 flex justify-between items-center">
          <Link to="/" className="text-xl sm:text-3xl font-bold tracking-tighter">
            MOVIELOG
          </Link>

          {/* 모바일용 햄버거 */}
          <div className="sm:hidden">
            <button onClick={() => setMenuOpen(!menuOpen)}>
              {menuOpen ? <FiX size={24} /> : <FiMenu size={24} />}
            </button>
          </div>

          {/* 데스크탑 메뉴 */}
          <nav className="hidden sm:flex items-center gap-3">
            {/* 데스크탑용 검색 */}
            <div className="items-center gap-4">
              <MovieSearchForm onSearch={() => setMenuOpen(false)} />
            </div>


            {isLoggedIn ? (
                <>
                  <Link to="/mypage">
                    <Button className="bg-gray-700 hover:bg-gray-600 text-white text-sm">
                      마이페이지
                    </Button>
                  </Link>
                  <Button
                      className="bg-gray-700 hover:bg-gray-600 text-white text-sm"
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

        {/* 모바일 메뉴 드롭다운 */}
        {menuOpen && (
            <div className="sm:hidden bg-gray-800 px-4 py-4 space-y-5" ref={menuRef}>
              <MovieSearchForm onSearch={() => setMenuOpen(false)} />
              <div className="space-x-2">
              {isLoggedIn ? (
                  <>
                    <Link to="/mypage">
                      <Button
                          className="w-full bg-gray-700 hover:bg-gray-600"
                          onClick={() => setMenuOpen(false)}
                      >
                        마이페이지
                      </Button>
                    </Link>
                    <Button
                        className="w-full bg-gray-700 hover:bg-gray-600"
                        onClick={() => {
                          logout();
                          setMenuOpen(false);
                        }}
                    >
                      로그아웃
                    </Button>
                  </>
              ) : (
                  // 로그인 버튼 회원가입 버튼 밖으로 꺼내야 됨
                  <>
                      <LoginDialog />
                      <SignupDialog />
                  </>
              )}
            </div>
            </div>
        )}
      </header>
  );
};

export default Header;
