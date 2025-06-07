import { useContext, useState } from "react";
import { Link } from "react-router-dom";
import { IoMdSearch } from "react-icons/io";
import { AuthContext } from "@/context/AuthContext.jsx";
import { LoginDialog } from "@/components/ui/LoginDialog.jsx";
import { SignupDialog } from "@/components/ui/SignupDialog.jsx";
import { Button } from "@/components/ui/button.jsx";
import { Input } from "@/components/ui/input.jsx";
import MovieSearchForm from "@/components/moive/search/MovieSearchForm.jsx";

const Header = () => {
    const { isLoggedIn, logout } = useContext(AuthContext);
    const [loginOpen, setLoginOpen] = useState(false);
    const [signupOpen, setSignupOpen] = useState(false);

    const handleSwitchToLogin = () => {
        setSignupOpen(false);
        setTimeout(() => setLoginOpen(true), 100);
    };

    const handleSwitchToSignup = () => {
        setLoginOpen(false);
        setTimeout(() => setSignupOpen(true), 100);
    };

    return (
        <header className="fixed w-full z-50 bg-gray-900 text-white top-0">
            <div className="w-full flex justify-between items-center py-3 container px-4">
                {/* 왼쪽: 로고 */}
                <Link to="/" className="text-xs sm:text-3xl tracking-tighter">
                    MOVIELOG
                </Link>

                {/* 오른쪽: 검색창 + 버튼들 */}
                <div className="flex items-center gap-3">
                    <MovieSearchForm/>

                    {!isLoggedIn ? (
                        <>
                            <Button variant="outline" onClick={() => setLoginOpen(true)}>
                                로그인
                            </Button>
                            <Button variant="outline" onClick={() => setSignupOpen(true)}>
                                회원가입
                            </Button>
                        </>
                    ) : (
                        <>
                            <img
                                src="https://i.pinimg.com/736x/2f/55/97/2f559707c3b04a1964b37856f00ad608.jpg"
                                alt="profile"
                                className="object-cover w-9 h-9 rounded-full border border-white"
                            />
                            <Link to="/mypage">
                                <Button variant="ghost">마이페이지</Button>
                            </Link>
                            <Button variant="ghost" onClick={logout}>
                                로그아웃
                            </Button>
                        </>
                    )}
                </div>
            </div>

            {/* 모달 */}
            <LoginDialog
                open={loginOpen}
                setOpen={setLoginOpen}
                onSwitch={handleSwitchToSignup}
            />
            <SignupDialog
                open={signupOpen}
                setOpen={setSignupOpen}
                onSwitch={handleSwitchToLogin}
            />
        </header>
    );
};

export default Header;
