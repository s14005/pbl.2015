/* DisplayPersonsByTypeStatus.java
 */

/* DisplayPersonsByTypeStatus
 */
public class DisplayPersonsByTypeStatus extends ConsoleStatus {

	// フィールド
	private String work;
	private PersonList plist;
	private PersonList selectedList;
	private DisplayPersonStatus next;
    private int next_start_id = 0;
    private int start_id;
    private int listsize;

	/**
	 * コンストラクタ DisplayPersonsByTypeStatus
	 * @param String firstMess
	 * @param String promptMess
	 * @param boolean IsEndStatus
	 * @param PersonList plist
	 * @param DisplayPersonStatus next
	 */
	DisplayPersonsByTypeStatus( String firstMess, String promptMess,
	                     boolean IsEndStatus,
	                     PersonList plist, DisplayPersonStatus next ) {
		super( firstMess, promptMess, IsEndStatus );
		this.work = "";
		this.plist = plist;
		this.selectedList = null;
		this.next = next;
	}

	// 最初に出力するメッセージを表示する
	/** displayFirstMess
	 * @throws Exception
	 */
	public void displayFirstMess() throws Exception {
		displayList(" ");
		super.displayFirstMess();
	}

	/** setWork
	 * @param String work
	 */
	public void setWork( String work ) {
		this.work = work;
	}

	// 入力された職種をもつ従業員のレコードだけを
	// 取り出す処理
	/**
	 * displayList
	 */
	public void displayList(String code) {
		// 入力された職種をもつ従業員のレコードだけを
		// selectedListに取り出す
        selectedList = plist.searchByTypes( work );
        listsize = selectedList.size();
        if( listsize <= 0 )
               System.out.println("従業員が存在しません。");
        else{
               if(code.equals(" ") && next_start_id == 0){
                       System.out.println("最初のページを表示");
                       int rows = listsize >= 3 ? 3 :listsize;
                       for(int i=0; i<rows; i++){
                               System.out.println( selectedList.getRecord(i).toString() );
                       }
                       start_id = next_start_id;
                       next_start_id = rows;
                }else if(code.equals("N")){
                       if(listsize > next_start_id){
                               System.out.println("次のページを表示");
                               int rows = listsize- next_start_id >= 3 ? 3 : listsize - next_start_id;
                               for(int i=next_start_id; i<next_start_id+rows; i++){
                                       System.out.println( selectedList.getRecord(i).toString() );
                               }
                               start_id = next_start_id;
                               next_start_id += rows;
                       }else{
                               System.out.println("最後まで表示して頭に戻りました");
                               int rows = listsize >= 3 ? 3 : listsize;
                               for(int i=0; i<rows; i++){
                                       System.out.println( selectedList.getRecord(i).toString() );
                               }
                               start_id = 0;
                               next_start_id = rows;
                       }
                }else if(code.equals("P")){
                        System.out.println("next_start_id:" + next_start_id);
                        System.out.println("start_id: " + start_id);
                        if(start_id >= 3){
                                System.out.println("前のページを表示");
                                if(next_start_id >= 6){
                                        next_start_id = start_id - 3;
                                }else{
                                        next_start_id = 0;
                                }
                                for(int i=next_start_id; i<next_start_id+3; i++){
                                        System.out.println( selectedList.getRecord(i).toString() );
                                }
                                start_id = next_start_id;
                                next_start_id += 3;
                        }else{
                                System.out.println("末尾の3件を表示");
                                next_start_id =
                                        listsize >= 3 ? listsize-3 : 0;
                                for(int i=next_start_id; i<listsize; i++){
                                        System.out.println( selectedList.getRecord(i).toString() );
                                }
                                start_id = next_start_id;
                                next_start_id = listsize;
                        }
                  }
             }
        }
	// 次の状態に遷移することを促すためのメッセージの表示
	/** getNextStatus
	 * @param String s
	 * @return ConsoleStatus
	 */
	public ConsoleStatus getNextStatus( String s ) {
		if(s.equals("N") || s.equals("P")){
                displayList(s);
                return this;
        }else{
                start_id = 0;
                next_start_id = 0;
        // 数値が入力された場合，その数値と同じIDをもつ
		// レコードがselectedListにあるかどうか判定し，
		// あればそれを次の状態DisplayPersonStatusに
                try {
                       int i = Integer.parseInt( s );
                       Person p = selectedList.get( i );
                       if( p == null )
                              return this;
                       else{
                              next.setPersonRecord( p );
                              return next;
                       }
                } catch( NumberFormatException e ){
                        return super.getNextStatus( s );
                }
        }
    }
}
