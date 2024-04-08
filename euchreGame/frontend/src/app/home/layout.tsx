export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <div>
      <div className="container mx-auto px-4">{children}</div>
    </div>
  );
}
