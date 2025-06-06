import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog.jsx";
import { Button } from "@/components/ui/button.jsx";
import { Input } from "@/components/ui/input.jsx";
import { Label } from "@/components/ui/label.jsx";

export function LoginDialog({ open, setOpen }) {
  return (
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="bg-gray-900 text-white rounded-md shadow-xl p-6 max-w-md w-full border border-gray-700">
          <DialogHeader>
            <DialogTitle className="text-lg font-semibold mb-4">
              로그인
            </DialogTitle>
          </DialogHeader>
          <form className="space-y-4">
            <div className="grid gap-2">
              <Label htmlFor="email">이메일</Label>
              <Input
                  id="email"
                  type="email"
                  placeholder="example@email.com"
                  className="bg-gray-800 text-white border border-gray-700 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div className="grid gap-2">
              <Label htmlFor="password">비밀번호</Label>
              <Input
                  id="password"
                  type="password"
                  className="bg-gray-800 text-white border border-gray-700 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <Button type="submit" className="w-full">
              로그인
            </Button>
          </form>
        </DialogContent>
      </Dialog>
  );
}
