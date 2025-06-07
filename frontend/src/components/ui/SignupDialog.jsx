import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog.jsx";
import { Button } from "@/components/ui/button.jsx";
import { Input } from "@/components/ui/input.jsx";
import { Label } from "@/components/ui/label.jsx";
import { useState } from "react";

export function SignupDialog({ open, setOpen, onSwitch }) {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSignup = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch("http://localhost:8080/api/auth/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          email,
          password,
          nickname: name
        })
      });

      const data = await response.json();

      if (!response.ok) {
        alert("회원가입 실패: " + (data.error || "알 수 없는 오류"));
        return;
      }

      alert("회원가입 성공! 로그인 해주세요.");
      setOpen(false);
      setTimeout(onSwitch, 100);
    } catch (err) {
      alert("회원가입 중 오류 발생: " + err.message);
    }
  };

  return (
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="bg-gray-900 text-white rounded-md shadow-xl p-6 max-w-md w-full border border-gray-700">
          <DialogHeader>
            <DialogTitle className="text-lg font-semibold mb-4">
              회원가입
            </DialogTitle>
          </DialogHeader>
          <form className="space-y-4" onSubmit={handleSignup}>
            <div className="grid gap-2">
              <Label htmlFor="name">이름</Label>
              <Input
                  id="name"
                  type="text"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  placeholder="홍길동"
                  className="bg-gray-800 text-white border border-gray-700 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="email">이메일</Label>
              <Input
                  id="email"
                  type="text"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  placeholder="example@email.com"
                  className="bg-gray-800 text-white border border-gray-700 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="password">비밀번호</Label>
              <Input
                  id="password"
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="bg-gray-800 text-white border border-gray-700 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <Button type="submit" className="w-full">
              회원가입
            </Button>
          </form>
          <p className="text-sm mt-4 text-center">
            이미 계정이 있으신가요?{" "}
            <span className="text-blue-400 cursor-pointer" onClick={onSwitch}>
            로그인
          </span>
          </p>
        </DialogContent>
      </Dialog>
  );
}
