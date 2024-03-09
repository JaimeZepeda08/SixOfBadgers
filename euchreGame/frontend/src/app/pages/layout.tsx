import TopNav from '../../components/TopNav';
 
export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <div>
      <TopNav />
      <div className="container mx-auto px-4">
        {children}
      </div>
    </div>

  );
}
