import { useState } from "react"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog.jsx"
import { Button } from "@/components/ui/button.jsx"
import { Input } from "@/components/ui/input.jsx"
import { Label } from "@/components/ui/label.jsx"

export function SignupDialog() {
  const [open, setOpen] = useState(false)
  const [name, setName] = useState("")
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")

  const handleSignup = () => {
    console.log("회원가입 시도:", { name, email, password })
    setOpen(false)
  }

  return (
    <>
      <Button id="open-login-btn" className="hidden" onClick={() => setOpen(true)}>
        로그인
      </Button>

      <Button id="open-signup-btn" className="hidden" onClick={() => setOpen(true)}>
        회원가입
      </Button>

      <Button
        className="rounded-sm border border-gray-500 hover:bg-gray-800 delay-3"
        onClick={() => setOpen(true)}
      >
        회원가입
      </Button>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="bg-gray-900/95 text-white rounded-xl shadow-2xl backdrop-blur-md px-8 py-6 max-w-md">
          <DialogHeader>
            <DialogTitle className="text-xl font-semibold text-center">회원가입</DialogTitle>
          </DialogHeader>

          <div className="grid gap-5 py-4">
            <div className="grid gap-2">
              <Label htmlFor="name">이름</Label>
              <Input
                id="name"
                value={name}
                onChange={(e) => setName(e.target.value)}
                className="bg-gray-800 text-white border border-gray-700 focus:ring-2 focus:ring-blue-500"
              />
            </div>
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
            <Button className="bg-blue-600 hover:bg-blue-700 transition-colors">회원가입</Button>
          </div>

          <div className="text-sm text-center text-gray-400 mt-2">
            이미 계정이 있으신가요?{" "}
            <span
              onClick={() => {
                setOpen(false)
                document.getElementById("open-login-btn")?.click()
              }}
              className="text-blue-400 hover:underline cursor-pointer"
            >
              로그인
            </span>
          </div>
        </DialogContent>
      </Dialog>
    </>
  )
}
