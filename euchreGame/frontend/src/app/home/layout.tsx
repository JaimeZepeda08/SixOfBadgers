
/**
 * Layout component used to wrap other components and provide a consistent layout.
 * 
 * @param {object} props The props object.
 * @param {React.ReactNode} props.children The child components to be wrapped by the layout.
 * @returns {JSX.Element} The layout component.
 */
export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <div>
      <div className="container mx-auto px-4">
        {children}
      </div>
    </div>

  );
}
