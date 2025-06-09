import { useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog.jsx";
import { Button } from "@/components/ui/button.jsx";
import {createReview} from "@/pages/api/reviewApi.js";

export function ReviewModal({ open, setOpen, movieId = null, fetchComments }) {
  const [content, setContent] = useState("");

  const handleSubmit = async () => {
    if (!movieId) {
      alert("영화 정보를 불러올 수 없습니다.");
      return;
    }

    try {
      await createReview({ movieId, content }); // rating 제외
      alert("리뷰가 등록되었습니다!");
      setOpen(false);
      setContent("");
      fetchComments?.(); // 코멘트 새로고침
    } catch (err) {
      console.error(err);
      alert("리뷰 등록 중 오류가 발생했습니다.");
    }

  };

  return (
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent
            className="bg-gray-900 p-6 rounded-md shadow-xl w-full max-w-md border border-gray-700 text-gray-100"
        >
          <DialogHeader>
            <DialogTitle className="text-lg font-semibold text-white mb-4">
              리뷰 작성
            </DialogTitle>
          </DialogHeader>

          <textarea
              value={content}
              onChange={(e) => setContent(e.target.value)}
              placeholder="이 영화에 대한 생각을 적어보세요"
              className="w-full min-h-[120px] text-sm px-3 py-2 rounded-md bg-gray-800 text-gray-100 border border-gray-600 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-blue-500"
          />

          <div className="flex justify-end gap-2 mt-4">
            <Button variant="secondary" onClick={() => setOpen(false)}>
              취소
            </Button>
            <Button onClick={handleSubmit} disabled={!content.trim()}>
              작성 완료
            </Button>
          </div>
        </DialogContent>
      </Dialog>
  );
}
