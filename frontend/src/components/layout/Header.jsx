import { useContext, useEffect, useRef, useState } from "react";
import { Link } from "react-router-dom";
import { FiMenu, FiX } from "react-icons/fi";
import { AuthContext } from "@/context/AuthContext.jsx";
import { Button } from "@/components/ui/button.jsx";
import { LoginDialog } from "@/components/ui/LoginDialog.jsx";
import { SignupDialog } from "@/components/ui/SignupDialog.jsx";
import MovieSearchForm from "@/components/moive/search/MovieSearchForm.jsx";

const Header = () => {
  const { isLoggedIn, logout } = useContext(AuthContext);
  const [menuOpen, setMenuOpen] = useState(false);
  const [loginOpen, setLoginOpen] = useState(false);
  const [signupOpen, setSignupOpen] = useState(false);
  const menuRef = useRef(null);

  const handleSwitchToLogin = () => {
    setSignupOpen(false);
    setTimeout(() => setLoginOpen(true), 100);
  };

  const handleSwitchToSignup = () => {
    setLoginOpen(false);
    setTimeout(() => setSignupOpen(true), 100);
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setMenuOpen(false);
      }
    };

    if (menuOpen) {
      document.addEventListener("mousedown", handleClickOutside);
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

        {/* 모바일 메뉴 토글 */}
        <div className="sm:hidden">
          <button onClick={() => setMenuOpen(!menuOpen)}>
            {menuOpen ? <FiX size={24} /> : <FiMenu size={24} />}
          </button>
        </div>

        {/* 데스크탑 메뉴 */}
        <nav className="hidden sm:flex items-center gap-3">
          <MovieSearchForm />
          {isLoggedIn ? (
            <>
              <img
                src="https://i.pinimg.com/736x/2f/55/97/2f559707c3b04a1964b37856f00ad608.jpg"
                alt="profile"
                className="object-cover w-9 h-9 rounded-full border border-white"
              />
              <Link to="/mypage">
                <Button variant="ghost">마이페이지</Button>
              </Link>
              <Button variant="ghost" onClick={logout}>로그아웃</Button>
            </>
          ) : (
            <>
              <Button variant="outline" onClick={() => setLoginOpen(true)}>로그인</Button>
              <Button variant="outline" onClick={() => setSignupOpen(true)}>회원가입</Button>
            </>
          )}
        </nav>
      </div>

      {/* 모바일 드롭다운 */}
      {menuOpen && (
        <div ref={menuRef} className="sm:hidden bg-gray-800 px-4 py-4 space-y-5">
          <MovieSearchForm />
          {isLoggedIn ? (
            <>
              <Link to="/mypage">
                <Button className="w-full bg-gray-700 hover:bg-gray-600" onClick={() => setMenuOpen(false)}>
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
            <>
              <Button className="w-full" onClick={() => {
                setLoginOpen(true);
                setMenuOpen(false);
              }}>
                로그인
              </Button>
              <Button className="w-full" onClick={() => {
                setSignupOpen(true);
                setMenuOpen(false);
              }}>
                회원가입
              </Button>
            </>
          )}
        </div>
      )}

      {/* 모달 */}
      <LoginDialog open={loginOpen} setOpen={setLoginOpen} onSwitch={handleSwitchToSignup} />
      <SignupDialog open={signupOpen} setOpen={setSignupOpen} onSwitch={handleSwitchToLogin} />
    </header>
  );
};

export default Header;