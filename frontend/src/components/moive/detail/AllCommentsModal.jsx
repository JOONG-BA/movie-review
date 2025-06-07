import {DialogHeader, Dialog, DialogContent, DialogTitle} from "@/components/ui/dialog.jsx";
import CommentCard from "@/components/moive/detail/CommentCard.jsx";

export default function AllCommentsModal({ open, setOpen, comments = [], movieTitle }) {
    return (
        <Dialog open={open} onOpenChange={setOpen} >
            <DialogContent className="bg-white text-black w-full sm:!max-w-xl md:!max-w-3xl lg:!max-w-5xl xl:!max-w-7xl !px-5">
                <DialogHeader>
                    <DialogTitle className="text-base md:text-lg font-semibold ">
                        {movieTitle} <br/>
                        리뷰 모아보기
                    </DialogTitle>
                </DialogHeader>

                <div className="grid gap-4 sm:grid-cols-2 md:grid-cols-3">
                    {comments.length > 0 ? (
                        comments.map((comment) => (
                            <CommentCard key={comment.id} comment={comment} />
                        ))
                    ) : (
                        <p className="text-center text-gray-500">아직 코멘트가 없습니다.</p>
                    )}
                </div>
            </DialogContent>
        </Dialog>
    );
}
