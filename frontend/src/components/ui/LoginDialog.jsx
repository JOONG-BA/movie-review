import { useState, useContext } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog.jsx"
import { Button } from "@/components/ui/button.jsx"
import { Input } from "@/components/ui/input.jsx"
import { Label } from "@/components/ui/label.jsx"
import { AuthContext } from "@/context/AuthContext.jsx" // ✅ 추가

export function LoginDialog() {
  const [open, setOpen] = useState(false)
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const { setIsLoggedIn } = useContext(AuthContext) // ✅ 전역 로그인 상태 제어

  const handleLogin = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ email, password })
      })

      const data = await response.json()

      if (!response.ok) {
        alert("로그인 실패: " + (data.error || "알 수 없는 오류"))
        return
      }

      localStorage.setItem("token", data.token)
      setIsLoggedIn(true) // ✅ 전역 로그인 상태 변경
      alert("로그인 성공!")
      setOpen(false)
    } catch (err) {
      alert("로그인 중 오류 발생: " + err.message)
    }
  }

  return (
    <>
      <Button className="rounded-sm border border-gray-500 hover:bg-gray-800 delay-3" onClick={() => setOpen(true)}>
        로그인
      </Button>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="bg-gray-900/95 text-white rounded-xl shadow-2xl backdrop-blur-md px-8 py-6 max-w-md">
          <DialogHeader>
            <DialogTitle className="text-xl font-semibold text-center">로그인</DialogTitle>
          </DialogHeader>

          <div className="grid gap-5 py-4">
            <div className="grid gap-2">
              <Label htmlFor="email">이메일</Label>
              <Input
                id="email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="bg-gray-800 text-white border border-gray-700 focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="password">비밀번호</Label>
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="bg-gray-800 text-white border border-gray-700 focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          <Button className="w-full bg-blue-600 hover:bg-blue-700 text-white" onClick={handleLogin}>
            로그인
          </Button>
        </DialogContent>
      </Dialog>
    </>
  )
}
