import { useState, useContext } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog.jsx"
import { Button } from "@/components/ui/button.jsx"
import { Input } from "@/components/ui/input.jsx"
import { Label } from "@/components/ui/label.jsx"
import { AuthContext } from "@/context/AuthContext.jsx"

export function LoginDialog() {
  const [open, setOpen] = useState(false)
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const { setIsLoggedIn } = useContext(AuthContext)

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
      setIsLoggedIn(true)
      alert("로그인 성공!")
      setOpen(false)
    } catch (err) {
      alert("로그인 중 오류 발생: " + err.message)
    }
  }

  return (
    <>
      <Button id="open-login-btn" className="hidden" onClick={() => setOpen(true)}>
        로그인
      </Button>

      <Button className="rounded-sm border border-gray-500 hover:bg-gray-800 delay-3" onClick={() => setOpen(true)}>
        로그인
      </Button>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="bg-gray-900/95 text-white">
          <DialogHeader>
            <DialogTitle>로그인</DialogTitle>
          </DialogHeader>

          <Label>이메일</Label>
          <Input value={email} onChange={(e) => setEmail(e.target.value)} />
          <Label>비밀번호</Label>
          <Input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />

          <Button className="mt-4" onClick={handleLogin}>
            로그인
          </Button>

          <p className="text-sm mt-4 text-center">
            아직 계정이 없으신가요?{' '}
            <span
              className="text-blue-400 cursor-pointer"
              onClick={() => {
                setOpen(false)
                document.getElementById("open-signup-btn")?.click()
              }}
            >
              회원가입
            </span>
          </p>
        </DialogContent>
      </Dialog>
    </>
  )
}
