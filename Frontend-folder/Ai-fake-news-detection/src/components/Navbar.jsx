const navItems = [
  { label: 'Home', href: '#home' },
  { label: 'Analyzer', href: '#analyzer' },
  { label: 'About', href: '#about' },
]

function Navbar() {
  return (
    <header className="sticky top-0 z-40 border-b border-white/10 bg-slate-950/65 backdrop-blur-xl">
      <nav className="mx-auto flex max-w-6xl items-center justify-between px-4 py-4 sm:px-6 lg:px-8">
        <a href="#home" className="font-orbitron text-sm font-bold uppercase tracking-[0.18em] text-cyan-300 sm:text-base">
          AI Fake News Detector
        </a>

        <ul className="flex items-center gap-2 sm:gap-4">
          {navItems.map((item) => (
            <li key={item.label}>
              <a
                href={item.href}
                className="rounded-full px-3 py-1.5 text-xs font-medium text-slate-300 transition hover:bg-cyan-400/10 hover:text-cyan-200 sm:text-sm"
              >
                {item.label}
              </a>
            </li>
          ))}
        </ul>
      </nav>
    </header>
  )
}

export default Navbar
