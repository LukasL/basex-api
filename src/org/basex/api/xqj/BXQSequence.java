package org.basex.api.xqj;

import static org.basex.api.xqj.BXQText.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.util.Properties;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQItem;
import javax.xml.xquery.XQItemType;
import javax.xml.xquery.XQResultSequence;
import org.basex.BaseX;
import org.basex.api.jaxp.IterStreamReader;
import org.basex.data.SAXSerializer;
import org.basex.data.XMLSerializer;
import org.basex.io.CachedOutput;
import org.basex.query.xquery.XQContext;
import org.basex.query.xquery.item.Item;
import org.basex.query.xquery.item.Type;
import org.basex.query.xquery.iter.Iter;
import org.basex.query.xquery.iter.SeqIter;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;

/**
 * Java XQuery API - Result Sequence.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-08, ISC License
 * @author Andreas Weiler
 */
public final class BXQSequence extends BXQAbstract implements XQResultSequence {
  /** Result iterator. */
  private final Iter result;
  /** Query context. */
  private final XQContext ctx;
  /** Static context. */
  private final BXQStaticContext sc;
  /** Current result. */
  private BXQItem it;
  /** Iterator position. */
  private int pos;
  /** BXQConnection. */
  private final BXQConnection conn;
  /** Next flag. */
  private boolean next;

  /**
   * Constructor.
   * @param item result item
   * @param context query context
   * @param c closer
   * @param scontext static context
   * @param connection connection
   */
  public BXQSequence(final Iter item, final XQContext context,
      final BXQAbstract c, final BXQStaticContext scontext,
      final BXQConnection connection) {
    super(c);
    result = item;
    ctx = context;
    sc = scontext;
    conn = connection;
  }

  public XQConnection getConnection() throws XQException {
    check();
    return conn;
  }

  public boolean absolute(final int p) throws XQException {
    final SeqIter iter = sequence();
    final int ps = p >= 0 ? p - 1 : iter.size + p;
    cursor(iter, ps);
    return ps >= 0 && ps < iter.size;
  }

  public void afterLast() throws XQException {
    final SeqIter iter = sequence();
    cursor(iter, iter.size);
  }

  public void beforeFirst() throws XQException {
    cursor(sequence(), -1);
  }

  public int count() throws XQException {
    return sequence().size;
  }

  public boolean first() throws XQException {
    return cursor(sequence(), 0);
  }

  public String getAtomicValue() throws XQException {
    return item().getAtomicValue();
  }

  public boolean getBoolean() throws XQException {
    return item().getBoolean();
  }

  public byte getByte() throws XQException {
    return item().getByte();
  }

  public double getDouble() throws XQException {
    return item().getDouble();
  }

  public float getFloat() throws XQException {
    return item().getFloat();
  }

  public int getInt() throws XQException {
    return item().getInt();
  }

  public XQItem getItem() throws XQException {
    return item();
  }

  public synchronized XMLStreamReader getItemAsStream() throws XQException {
    check();
    return item().getItemAsStream();
  }

  public String getItemAsString(final Properties p) throws XQException {
    return item().getItemAsString(p);
  }

  public XQItemType getItemType() throws XQException {
    pos();
    return it.getItemType();
  }

  public long getLong() throws XQException {
    return item().getLong();
  }

  public Node getNode() throws XQException {
    return item().getNode();
  }

  public URI getNodeUri() throws XQException {
    pos();
    return it.getNodeUri();
  }

  public Object getObject() throws XQException {
    return item().getObject();
  }

  public int getPosition() throws XQException {
    final SeqIter iter = sequence();
    return pos != -1 ? pos : iter.size + 1;
  }

  public synchronized XMLStreamReader getSequenceAsStream() throws XQException {
    check();
    if(it != null && !next) throw new BXQException(TWICE);
    return new IterStreamReader(result);
  }

  public String getSequenceAsString(final Properties p) throws XQException {
    check();
    if(it != null && !next) throw new BXQException(TWICE);
    if(!next && !next()) return "";
    
    final CachedOutput out = new CachedOutput();
    final XMLSerializer ser = new XMLSerializer(out);
    try {
      do {
        final Item item = item().it;
        if(item.type == Type.ATT) throw new BXQException(ATTR);
        item.serialize(ser, ctx, 0);
      } while(next());
    } catch(final IOException ex) {
      throw new BXQException(ex);
    }
    return out.toString();
  }

  public short getShort() throws XQException {
    return item().getShort();
  }

  public boolean instanceOf(final XQItemType type) throws XQException {
    pos();
    return it.instanceOf(type);
  }

  public boolean isAfterLast() throws XQException {
    sequence();
    return pos == -1;
   }

  public boolean isBeforeFirst() throws XQException {
    sequence();
    return pos == 0;
  }

  public boolean isFirst() throws XQException {
    sequence();
    return pos == 1;
  }

  public boolean isLast() throws XQException {
    final SeqIter iter = sequence();
    return pos == iter.size;
  }

  public boolean isOnItem() throws XQException {
    check();
    return pos > 0;
  }

  public boolean isScrollable() throws XQException {
    check();
    return sc.scrollable;
  }

  public boolean last() throws XQException {
    final SeqIter seq = sequence();
    return cursor(seq, seq.size);
  }

  public boolean next() throws XQException {
    check();
    if(pos < 0) return false;

    try {
      final Item i = result.next();
      next = i != null;
      pos++;
      it = new BXQItem(i, this, ctx, conn);
      if(!next) pos = -1;
      return next;
    } catch(final org.basex.query.xquery.XQException ex) {
      throw new BXQException(ex);
    }
  }

  public boolean previous() throws XQException {
    return cursor(sequence(), pos - 1);
  }

  public boolean relative(final int p) throws XQException {
    return absolute(pos + p);
  }

  public void writeItem(final OutputStream os, final Properties p)
      throws XQException {
    item().writeItem(os, p);
  }

  public void writeItem(final Writer ow, final Properties p)
      throws XQException {
    item().writeItem(ow, p);
  }

  public void writeItemToResult(final Result r) throws XQException {
    item().writeItemToResult(r);
  }

  public void writeItemToSAX(final ContentHandler sax) throws XQException {
    item().writeItemToSAX(sax);
  }

  public void writeSequence(final OutputStream os, final Properties p)
      throws XQException {
    if(it != null && !next) throw new BXQException(TWICE);
    while(next()) item().writeItem(os, p);
  }

  public void writeSequence(final Writer ow, final Properties p)
      throws XQException {
    if(it != null && !next) throw new BXQException(TWICE);
    while(next()) item().writeItem(ow, p);
  }

  public void writeSequenceToResult(final Result result) throws XQException {
    check(result, Result.class);
    if(it != null && !next) throw new BXQException(TWICE);

    // evaluate different Result types...
    if(result instanceof StreamResult) {
      // StreamResult.. directly write result as string
      writeSequence(((StreamResult) result).getWriter(), null);
    } else if(result instanceof SAXResult) {
      // SAXResult.. serialize result to underlying parser
      final SAXSerializer ser = new SAXSerializer(null);
      ContentHandler h = ((SAXResult) result).getHandler();
      ser.setContentHandler(h);
      while(next()) serialize(item().it, ctx, ser);
    } else {
      BaseX.notimplemented();
    }
  }

  public void writeSequenceToSAX(final ContentHandler sax) throws XQException {
    check(sax, ContentHandler.class);
    writeSequenceToResult(new SAXResult(sax));
  }

  /**
   * Checks the specified cursor position.
   * @return item
   * @throws XQException xquery exception
   */
  private BXQItem item() throws XQException {
    pos();
    if(!next) throw new BXQException(TWICE);
    next = sc.scrollable;
    return it;
  }

  /**
   * Checks the specified cursor position.
   * @throws XQException xquery exception
   */
  private void pos() throws XQException {
    if(!isOnItem()) throw new BXQException(CURSOR);
  }

  /**
   * Checks the forward flag and returns the result.
   * @return sequence iterator
   * @throws XQException xquery exception
   */
  private SeqIter sequence() throws XQException {
    check();
    if(!sc.scrollable) throw new BXQException(FORWARD);
    return (SeqIter) result;
  }

  /**
   * Sets the cursor to the specified position.
   * @param seq iterator sequence
   * @param p cursor position
   * @return result of check
   * @throws XQException xquery exception
   */
  private boolean cursor(final SeqIter seq, final int p) throws XQException {
    pos = p == -1 ? 0 : p > seq.size ? -1 : p;
    seq.pos = pos - 1;
    return p == -1 ? true : next();
  }
}