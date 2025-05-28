export default function Footer() {
  return (
    <footer className="bg-gray-900 text-gray-400 py-6 mt-10">
      <div className="container mx-auto text-center space-y-2">
        <div className="text-xl text-white font-semibold">🎬 MOVIELOG</div>
        <div className="text-sm">2025 Open Source Project</div>
        <div className="flex justify-center gap-6 text-sm">
          <a href="https://github.com/JOONG-BA/movie-review" className="hover:text-white" target="_blank">
            GitHub
          </a>
          <a href="https://www.notion.so/1f11f27b0d5d8036ac3dcfcdc405d580t" className="hover:text-white" target="_blank">
            Notion
          </a>
        </div>
      </div>
    </footer>
  )
}