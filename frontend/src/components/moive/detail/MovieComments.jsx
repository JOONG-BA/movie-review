import {Link} from "react-router-dom";
import CommentCard from "@/components/moive/detail/CommentCard.jsx";
import {Button} from "@/components/ui/button.jsx";


export default function MovieComments({ comments = [], setModalOpen, setShowAllModal }) {
    const MAX_LENGTH = 60; // 자를 최대 글자 수
    return(
        <div className="mt-8 container">
            <div className="flex items-end my-4">
                <h2 className="flex-1 text-xl text-left font-bold">코멘트</h2>
                {comments.length > 0 && (
                    <Button className="text-xs text-gray-500" onClick={() => setShowAllModal(true)}>
                        더보기
                    </Button>
                )}
            </div>

            {comments.filter(comment => comment && comment.content !== null && comment.content !== undefined).length > 0 ? (
                <div className="flex sm:grid gap-4 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 overflow-hidden ">
                    {comments.filter(comment => comment && comment.content !== null && comment.content !== undefined).map(comment => (
                        <CommentCard key={comment.id} comment={comment} maxLength={MAX_LENGTH} />
                    ))}
                </div>
            ) : (
                <div className="w-full p-6 bg-gray-100 text-center shadow-sm">
                    <p className="text-gray-700 text-sm mb-2">아직 등록된 코멘트가 없어요.</p>
                    <p className="text-lg font-semibold text-gray-800">첫 번째 감상평을 남겨보세요</p>
                    <button
                        onClick={() => setModalOpen(true)}
                        className="mt-4 px-4 py-2 bg-gray-800 hover:bg-gray-900 text-white text-sm rounded-md transition"
                    >
                        코멘트 작성하기
                    </button>
                </div>
            )}
        </div>
    );
}

